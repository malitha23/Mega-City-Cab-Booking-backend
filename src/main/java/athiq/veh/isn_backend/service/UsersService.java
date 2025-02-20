package athiq.veh.isn_backend.service;
import athiq.veh.isn_backend.constant.RoleAuthorityEnum;
import athiq.veh.isn_backend.dto.auth.UserDTO;
import athiq.veh.isn_backend.dto.response.DefaultResponseDTO;
import athiq.veh.isn_backend.dto.response.UserResponseDTO;
import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import athiq.veh.isn_backend.mapper.BlobMapper;
import athiq.veh.isn_backend.model.Role;
import athiq.veh.isn_backend.security.service.UserDetailsImpl;
import org.springframework.transaction.annotation.Transactional;

import athiq.veh.isn_backend.dto.auth.ChangePasswordRequestDTO;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.RoleRepository;
import athiq.veh.isn_backend.repository.UserRepository;
import athiq.veh.isn_backend.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BlobService blobService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;

    public String getUserEmailFromJwtToken(String token) {
        return jwtUtils.getUserNameFromJwtToken(token);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Transactional
    public ResponseEntity<String> uploadImageForUser(MultipartFile file, String token) {
        try {
            // Extract JWT token
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Get email from JWT token
            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

            // Fetch user
            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user = optionalUser.orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
            }

            // **Delete previous image**
            if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                String previousImagePath = user.getImageUrl().replace(
                        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/files/").toUriString(),
                        "uploads/"
                );

                Path previousFilePath = Paths.get(previousImagePath);
                try {
                    Files.deleteIfExists(previousFilePath);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to delete previous image: " + e.getMessage());
                }
            }

            // Save new file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "uploads";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);

            // Use try-with-resources to ensure the stream is closed properly
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Generate new file URL
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(fileName)
                    .toUriString();

            // Update user's imageUrl
            user.setImageUrl(fileUrl);
            userRepository.save(user);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }




    @Transactional
    public ResponseEntity<String> updateUserDetails(String firstName, String lastName, String email, String token) {
        try {
            // Extract JWT token from Authorization header
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Get email from JWT token
            String emailget = jwtUtils.getUserNameFromJwtToken(jwtToken);
            logger.debug("Extracted email from JWT token: {}", emailget);

            // Fetch user
            Optional<User> optionalUser = userRepository.findByEmail(emailget);
            User user = optionalUser.orElse(null);

            if (user == null) {
                logger.warn("User with email {} not found", emailget);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Update the user's first and last names
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            userRepository.save(user);

            // If the email is changing, send a verification email
            if (!emailget.equals(email)) {

                String subject = "Your New Email Address Is Updated";
                String body = "<p>Hello, " + firstName + "!</p>" +
                        "<p>Your new email address has been updated. Please log in using the new email address.</p>";

                emailService.sendEmail(email, subject, body);

                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Email change initiated. Please verify your new email.");
            }


            logger.info("User details updated successfully for email {}", email);
            return ResponseEntity.ok("User details updated successfully");
        } catch (Exception e) {
            logger.error("Unexpected error updating user details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }


    public UserResponseDTO getUserData(String token) {

        logger.info("Received request to get user data");
        logger.info("Authorization token: {}", token);

        // Extract JWT token from Authorization header
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Get email from JWT token
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        logger.debug("Extracted email from JWT token: {}", email);

        // Fetch user
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();


            return new UserResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getImageUrl(),
                    BlobMapper.INSTANCE.toDto(user.getImageBlob()),
                    user.getRole()
            );
        } else {
            throw new ResourceNotFoundException("user not found");
        }
    }


    // Change password service
    public ResponseEntity<String> changePassword(ChangePasswordRequestDTO request, String token) {
        try {
            // Extract JWT token from Authorization header
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Get email from JWT token
            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
            logger.debug("Extracted email from JWT token: {}", email);

            // Fetch user
            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user = optionalUser.orElse(null);

            if (user == null) {
                logger.warn("User with email {} not found", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Encode the new password
            String encodedPassword = passwordEncoder.encode(request.getNewPassword());

            // Update the user's password
            user.setPassword(encodedPassword);
            userRepository.save(user);
            logger.info("Password updated successfully for user with email {}", email);

            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            logger.error("Unexpected error updating password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    // Helper method to save the file
    private String saveFile(MultipartFile file) throws IOException {
        // Generate a unique file name using the current timestamp
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String uploadDir = "src/main/resources/static/uploads";
        File uploadDirectory = new File(uploadDir);

        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();  // Create the directory if it doesn't exist
        }

        // Construct the file path
        Path path = Paths.get(uploadDir, fileName);
        Files.write(path, file.getBytes());

        // Generate the file's accessible URL
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();
    }


    @Transactional
    public User changeUserRoleToAdmin(String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found with email: " + email));

        // Find the role by authority enum
        Role adminRole = roleRepository.findByAuthority(RoleAuthorityEnum.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Role Not Found with authority: ROLE_ADMIN"));

        // Set the user's role to admin
        user.setRole(adminRole);

        // Save the updated user and return it
        return userRepository.save(user);
    }


    @Transactional
    public ResponseEntity<?> deactivateCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getUsername(); // Get the email of the currently logged-in user

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!user.getIsActivated()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new DefaultResponseDTO("User is already deactivated.", null));
                }

                user.setIsActivated(false); // Set to false
                userRepository.save(user);

                return ResponseEntity.ok(new DefaultResponseDTO("User has been deactivated successfully.", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DefaultResponseDTO("User not found.", null));
            }
        } catch (Exception e) {
            logger.error("Failed to deactivate user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DefaultResponseDTO("Failed to deactivate user.", null));
        }
    }


//    public ResponseEntity<?> getLoggedInUserDesignation(String token) {
//        try {
//            // Extract JWT token from Authorization header
//            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
//
//            // Get email from JWT token
//            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
//
//            // Fetch user by email
//            Optional<User> optionalUser = userRepository.findByEmail(email);
//            if (optionalUser.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            User user = optionalUser.get();
//
//            // Fetch user's designation
//            Designation designation = user.getDesignation();
//            if (designation == null) {
//                return ResponseEntity.status(HttpStatus.OK).body("No designation assigned");
//            }
//
////            Map<String, Object> response = new HashMap<>();
////            response.put("name", designation.getName());
////            response.put("image_url", designation.getImageUrl());  // Assuming Designation has getImageUrl() method
////
//
//            // Return the designation information
//            return ResponseEntity.ok(designation.getName());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching designation: " + e.getMessage());
//        }
//    }



}











//
//
//
//
//package com.jxg.isn_backend.service;
//import com.jxg.isn_backend.constant.RoleAuthorityEnum;
//import com.jxg.isn_backend.dto.response.DefaultResponseDTO;
//import com.jxg.isn_backend.model.Designation;
//import com.jxg.isn_backend.model.FileBlob;
//import com.jxg.isn_backend.model.Role;
//import com.jxg.isn_backend.repository.DesignationRepository;
//import com.jxg.isn_backend.security.service.UserDetailsImpl;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.jxg.isn_backend.dto.auth.ChangePasswordRequestDTO;
//import com.jxg.isn_backend.model.User;
//import com.jxg.isn_backend.repository.RoleRepository;
//import com.jxg.isn_backend.repository.UserRepository;
//import com.jxg.isn_backend.security.jwt.JwtUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Service
//public class UsersService {
//
//    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private BlobService blobService;
//
//    @Autowired
//    private DesignationRepository designationRepository;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    public String getUserEmailFromJwtToken(String token) {
//        return jwtUtils.getUserNameFromJwtToken(token);
//    }
//
//    public Optional<User> findUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    @Transactional
//    public ResponseEntity<String> uploadImageForUser(MultipartFile file, String token) {
//        try {
//            // Extract JWT token from Authorization header
//            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
//
//            // Get email from JWT token
//            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
//
//            // Fetch user
//            Optional<User> optionalUser = userRepository.findByEmail(email);
//            User user = optionalUser.orElse(null);
//
//            if (user == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            // Save the file using BlobService
//            FileBlob fileBlob = blobService.saveBlobToLocal("src/main/resources/static/uploads", file);
//
//            // Update user's imageBlob reference
//            user.setImageBlob(fileBlob);
//            userRepository.save(user);
//
//            return ResponseEntity.ok("Image uploaded successfully");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
//        }
//    }
//
//
//    public ResponseEntity<String> updateUserDetails(String firstName, String lastName, String token) {
//        try {
//            // Extract JWT token from Authorization header
//            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
//
//            // Get email from JWT token
//            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
//            logger.debug("Extracted email from JWT token: {}", email);
//
//            // Fetch user
//            Optional<User> optionalUser = userRepository.findByEmail(email);
//            User user = optionalUser.orElse(null);
//
//            if (user == null) {
//                logger.warn("User with email {} not found", email);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            // Update the user's first and last names
//            user.setFirstName(firstName);
//            user.setLastName(lastName);
//            userRepository.save(user);
//            logger.info("User details updated successfully for email {}", email);
//
//            return ResponseEntity.ok("User details updated successfully");
//        } catch (Exception e) {
//            logger.error("Unexpected error updating user details", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Unexpected error: " + e.getMessage());
//        }
//    }
//
////
////    @Transactional
////    public ResponseEntity<String> updateUserDetails(String firstName, String lastName, Long designationId, String token) {
////        try {
////            // Extract JWT token from Authorization header
////            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
////
////            // Get email from JWT token
////            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
////            logger.debug("Extracted email from JWT token: {}", email);
////
////            // Fetch user
////            Optional<User> optionalUser = userRepository.findByEmail(email);
////            User user = optionalUser.orElse(null);
////
////            if (user == null) {
////                logger.warn("User with email {} not found", email);
////                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
////            }
////
////            // Update the user's first and last names
////            user.setFirstName(firstName);
////            user.setLastName(lastName);
////
////            // Update designation if provided
////            if (designationId != null) {
////                Integer designationIdInt = designationId.intValue();
////                Optional<Designation> optionalDesignation = designationRepository.findById(designationIdInt);
////                if (optionalDesignation.isPresent()) {
////                    user.setDesignation(optionalDesignation.get());
////                } else {
////                    return ResponseEntity.badRequest().body("Invalid designation ID.");
////                }
////            } else {
////                user.setDesignation(null); // Clear designation if ID is null
////            }
////
////            userRepository.save(user);
////            logger.info("User details updated successfully for email {}", email);
////
////            return ResponseEntity.ok("User details updated successfully");
////        } catch (Exception e) {
////            logger.error("Unexpected error updating user details", e);
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .body("Unexpected error: " + e.getMessage());
////        }
////    }
//
//
//
//    public User createUserWithDesignation(User user, Integer designationId) {
//        designationRepository.findById(designationId).ifPresent(user::setDesignation);
//        return userRepository.save(user);
//    }
//
//    // Change password service
//    public ResponseEntity<String> changePassword(ChangePasswordRequestDTO request, String token) {
//        try {
//            // Extract JWT token from Authorization header
//            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
//
//            // Get email from JWT token
//            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
//            logger.debug("Extracted email from JWT token: {}", email);
//
//            // Fetch user
//            Optional<User> optionalUser = userRepository.findByEmail(email);
//            User user = optionalUser.orElse(null);
//
//            if (user == null) {
//                logger.warn("User with email {} not found", email);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            // Encode the new password
//            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
//
//            // Update the user's password
//            user.setPassword(encodedPassword);
//            userRepository.save(user);
//            logger.info("Password updated successfully for user with email {}", email);
//
//            return ResponseEntity.ok("Password updated successfully");
//        } catch (Exception e) {
//            logger.error("Unexpected error updating password", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Unexpected error: " + e.getMessage());
//        }
//    }
//
//    // Helper method to save the file
//    private String saveFile(MultipartFile file) throws IOException {
//        // Generate a unique file name using the current timestamp
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        String uploadDir = "src/main/resources/static/uploads";
//        File uploadDirectory = new File(uploadDir);
//
//        if (!uploadDirectory.exists()) {
//            uploadDirectory.mkdirs();  // Create the directory if it doesn't exist
//        }
//
//        // Construct the file path
//        Path path = Paths.get(uploadDir, fileName);
//        Files.write(path, file.getBytes());
//
//        // Generate the file's accessible URL
//        return ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/uploads/")
//                .path(fileName)
//                .toUriString();
//    }
//
//
//    @Transactional
//    public User changeUserRoleToAdmin(String email) {
//        // Find the user by email
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User Not Found with email: " + email));
//
//        // Find the role by authority enum
//        Role adminRole = roleRepository.findByAuthority(RoleAuthorityEnum.ROLE_ADMIN)
//                .orElseThrow(() -> new RuntimeException("Role Not Found with authority: ROLE_ADMIN"));
//
//        // Set the user's role to admin
//        user.setRole(adminRole);
//
//        // Save the updated user and return it
//        return userRepository.save(user);
//    }
//
//
//    @Transactional
//    public ResponseEntity<?> deactivateCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        String email = userDetails.getUsername(); // Get the email of the currently logged-in user
//
//        try {
//            Optional<User> optionalUser = userRepository.findByEmail(email);
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                if (!user.getIsActivated()) {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body(new DefaultResponseDTO("User is already deactivated.", null));
//                }
//
//                user.setIsActivated(false); // Set to false
//                userRepository.save(user);
//
//                return ResponseEntity.ok(new DefaultResponseDTO("User has been deactivated successfully.", null));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new DefaultResponseDTO("User not found.", null));
//            }
//        } catch (Exception e) {
//            logger.error("Failed to deactivate user", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new DefaultResponseDTO("Failed to deactivate user.", null));
//        }
//    }
//
//}