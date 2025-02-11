package athiq.veh.isn_backend.controller.auth;

import athiq.veh.isn_backend.dto.auth.*;
import athiq.veh.isn_backend.dto.response.DefaultResponseDTO;
import athiq.veh.isn_backend.exception.UserNotActivatedException;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.UserRepository;
import athiq.veh.isn_backend.security.jwt.JwtUtils;
import athiq.veh.isn_backend.security.service.UserDetailsImpl;
import athiq.veh.isn_backend.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final AuthService authService;


    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtils jwtUtils, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }


    private String getRole(User user) {
        return user.getRole() != null ? user.getRole().getAuthority().name() : "Unknown"; // Get role as string
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findUserById(userDetails.getId());

        if (!user.getIsActivated()) {
            throw new UserNotActivatedException("User Not Activated");
        }

        String jwt = jwtUtils.generateJwtToken(authentication);

//        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();


        UserMinDTO userMinDTO = new UserMinDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getImageUrl(),
                user.getImageBlob(),
                getRole(user));

        return ResponseEntity.ok(new SignInResponseDTO(jwt, userMinDTO, userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDTO signUpRequest) {

        User user = this.authService.signup(signUpRequest);

        return ResponseEntity.ok(new SignUpOTPResponseDTO("User signing is initiated." +
                "An OTP sent to your organization email ", user.getEmail(), user.getOtpExpireDate()));

    }

    @PostMapping("/otp/validate")
    public ResponseEntity<?> validateOtp(@RequestBody OtpValidateRequestDTO otpRequest) {

        User user = this.authService.validateOtp(otpRequest);

        return ResponseEntity.ok(new DefaultResponseDTO("User \"" + user.getEmail() + "\" activated successfully", null));

    }

    @PostMapping("/otp/resend")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequestDTO otpRequestDTO) {
        User user = this.authService.regenerateOtp(otpRequestDTO);

        return ResponseEntity.ok(new DefaultResponseDTO("OTP sent to email: \"" + user.getEmail(), null));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO) {
        try {
            authService.sendPasswordResetToken(requestDTO.email());
            return ResponseEntity.ok(new DefaultResponseDTO("Password reset instructions have been sent to your email.", null));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponseDTO("An error occurred. Please try again.", null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequestDTO requestDTO) {
        try {
            authService.resetPassword(requestDTO.token(), requestDTO.newPassword());
            return ResponseEntity.ok(new DefaultResponseDTO("Password has been reset successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DefaultResponseDTO("Invalid or expired token.", null));
        }
    }

    @GetMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String otp) {
        // Handle OTP verification logic here
        // e.g., display a form for the user to enter a new password

        return ResponseEntity.ok("OTP received, please enter your new password.");
    }

}
