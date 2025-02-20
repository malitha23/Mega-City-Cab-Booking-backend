package athiq.veh.isn_backend.controller.auth;
import athiq.veh.isn_backend.dto.response.UserResponseDTO;
import org.springframework.web.bind.annotation.*;
import athiq.veh.isn_backend.dto.auth.ChangePasswordRequestDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UsersService userService;

    @PutMapping("/uploadImage")
    public ResponseEntity<String> uploadImageForUser(@RequestParam("file") MultipartFile file,
                                                     @RequestHeader("Authorization") String token) {
        return userService.uploadImageForUser(file, token);
    }

    @GetMapping("/image")
    public ResponseEntity<Map<String, String>> getUserImageUrl(@RequestHeader("Authorization") String token) {
        try {
            // Extract the JWT token from the Authorization header
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String email = userService.getUserEmailFromJwtToken(jwtToken); // Extract email from token

            // Find the user by email
            Optional<User> optionalUser = userService.findUserByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Get image URL
                String imageUrl = user.getImageUrl();

                // Prepare response
                Map<String, String> response = new HashMap<>();
                response.put("imageUrl", imageUrl != null ? imageUrl : "");

                return ResponseEntity.ok(response); // Return the image URL as JSON
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // User not found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
        }
    }

    @PutMapping("/updateDetails")
    public ResponseEntity<String> updateUserDetails(@RequestParam("firstName") String firstName,
                                                    @RequestParam("lastName") String lastName,
                                                    @RequestParam("email") String email,
                                                    @RequestHeader("Authorization") String token) {
        logger.info("Received request to update user details");
        logger.info("First name: {}", firstName);
        logger.info("Last name: {}", lastName);
        logger.info("Authorization token: {}", token);

        return userService.updateUserDetails(firstName, lastName, email, token);
    }

//    @GetMapping("/profile")
//    public ResponseEntity<String> getProfile(@RequestHeader("Authorization") String token) {
//        logger.info("Received request to get user profile");
//        return userService.getProfile(token);
//    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO request,
                                                 @RequestHeader("Authorization") String token) {
        logger.info("Received request to change password");
        logger.info("Authorization token: {}", token);

        return userService.changePassword(request, token);
    }

   @GetMapping("/getUserData")
   public UserResponseDTO getUserData(@RequestHeader("Authorization") String token) {
     return userService.getUserData(token);  // Ensure this method returns ResponseEntity<UserResponseDTO>
   }


    @PutMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email of the currently logged-in user

        logger.info("Received request to deactivate account for user: {}", email);

        return userService.deactivateCurrentUser();
    }




}