package athiq.veh.isn_backend.service;

import athiq.veh.isn_backend.constant.RoleAuthorityEnum;
import athiq.veh.isn_backend.dto.auth.OtpValidateRequestDTO;
import athiq.veh.isn_backend.dto.auth.ResendOtpRequestDTO;
import athiq.veh.isn_backend.dto.auth.SignUpRequestDTO;
import athiq.veh.isn_backend.exception.BadRequestException;
import athiq.veh.isn_backend.exception.OtpAuthenticationFailedException;
import athiq.veh.isn_backend.model.Role;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.RoleRepository;
import athiq.veh.isn_backend.repository.UserRepository;
import athiq.veh.isn_backend.security.service.UserDetailsImpl;
import athiq.veh.isn_backend.util.RandomPasswordUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User signup(SignUpRequestDTO signUpRequest) {

        // Ensure email is not modified unintentionally
        String email = signUpRequest.email();
        System.out.println("Received email: " + email); // Log to verify

        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new BadRequestException("Email isn't available. \n Contact support");
        }

        Optional<Role> userRoleOptional = roleRepository.findByAuthority(RoleAuthorityEnum.ROLE_USER);

        if (userRoleOptional.isEmpty()) {
            throw new RuntimeException("Couldn't apply roles.");
        }

        String otp = RandomPasswordUtil.generateOTP(6);
        LocalDateTime otpExpiration = LocalDateTime.now().plusMinutes(31);

        this.emailService.sendEmail(signUpRequest.email(), "User Account Activation", this.createOtmEmailBody(otp, otpExpiration));

        System.out.println("Generated OTP: " + otp);
        System.out.println("OTP sent in email: " + otp);


        User user = new User();
        user.setEmail(signUpRequest.email());
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
//        user.setOtp(RandomPasswordUtil.generateOTP(6));
        user.setOtp(otp);
        user.setOtpExpireDate(LocalDateTime.now().plusMinutes(30));
        user.setIsActivated(false);
        user.setIsAdminRenewed(false); // only for admin users
        user.setEnabled(true);
        user.setRole(userRoleOptional.get());
        user.setEnabled(true);


        User savedUser = userRepository.save(user);

        savedUser.setCreatedBy(savedUser);
        savedUser.setLastModifiedBy(savedUser);
        userRepository.save(savedUser);

        return savedUser;

    }


    // sign in

    public User validateOtp(OtpValidateRequestDTO requestDTO) {

        Optional<User> optionalUser = this.userRepository.findByEmail(requestDTO.email());
        if(optionalUser.isEmpty()) {
            throw new OtpAuthenticationFailedException("Invalid user");
        }

        if (optionalUser.get().getOtpExpireDate().isBefore(LocalDateTime.now())) {
            throw new OtpAuthenticationFailedException("OTP expired.");
        }

        if (!optionalUser.get().getOtp().equals(requestDTO.otp())) {
            throw new OtpAuthenticationFailedException("Invalid OTP.");
        }

        optionalUser.get().setIsActivated(true);
        optionalUser.get().setOtpExpireDate(LocalDateTime.now());
        userRepository.save(optionalUser.get());

        return optionalUser.get();
    }

    public User regenerateOtp(ResendOtpRequestDTO requestDTO) {

        Optional<User> optionalUser = this.userRepository.findByEmail(requestDTO.email());
        if(optionalUser.isEmpty()) {
            throw new BadRequestException("Invalid user");
        }

        if (optionalUser.get().getIsActivated()) {
            throw new BadRequestException("User already activated");
        }

        String otp = RandomPasswordUtil.generateOTP(6);
        LocalDateTime otpExpiration = LocalDateTime.now().plusMinutes(31);

        this.emailService.sendEmail(optionalUser.get().getEmail(), "User Account Activation", this.createOtmEmailBody(otp, otpExpiration));

        optionalUser.get().setOtp(otp);
        optionalUser.get().setOtpExpireDate(otpExpiration);
        userRepository.save(optionalUser.get());

        return optionalUser.get();
    }


    public User getCurrentLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public void sendPasswordResetToken(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BadRequestException("No account found with that email address.");
        }

        User user = userOptional.get();
        String resetToken = RandomPasswordUtil.generateOTP(6);
        LocalDateTime tokenExpiration = LocalDateTime.now().plusHours(1);

        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiration(tokenExpiration);
        userRepository.save(user);


        String resetUrl = "http://172.20.10.3:5173/reset-password?token="+resetToken;
        String emailBody = createPasswordResetEmailBody(resetUrl);

        emailService.sendEmail(email, "Password Reset Request", emailBody);
    }


    public void resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByPasswordResetToken(token);
        if (userOptional.isEmpty()) {
            throw new BadRequestException("Invalid or expired token.");
        }

        User user = userOptional.get();
        if (user.getPasswordResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expired.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiration(null);
        userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User with ID " + userId + " not found"));
    }

    public List<User> findUsersByFirstOrLastName(String name) {
        return userRepository.findByFirstNameContainingIgnoreCase(name);
    }

    private String createPasswordResetEmailBody(String resetUrl) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Password Reset</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>We received a request to reset your password. Click the link below to complete the reset process:</p>\n" +
                "    <p><a href=\"" + resetUrl + "\">Reset Password</a></p>\n" +
                "    <p>If you did not request this, please ignore this email.</p>\n" +
                "</body>\n" +
                "</html>";
    }



    private String createOtmEmailBody(String otp, LocalDateTime otmExpiration) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>OTP Verification</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding: 10px 0;\n" +
                "            background-color: #FF8C00;\n" +
                "            color: #ffffff;\n" +
                "            border-radius: 8px 8px 0 0;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .otp {\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "        .expiration {\n" +
                "            margin-top: 10px;\n" +
                "            font-size: 14px;\n" +
                "            color: #888888;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding: 10px 0;\n" +
                "            font-size: 12px;\n" +
                "            color: #888888;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>OTP Verification</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Your One-Time Password (OTP) is:</p>\n" +
                "            <p class=\"otp\">" + otp + "</p>\n" +
                "            <p class=\"expiration\">This OTP is valid until " + otmExpiration + ".</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you did not request this OTP, please ignore this email.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
