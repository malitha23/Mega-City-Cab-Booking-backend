package athiq.veh.isn_backend.controller.auth;


import athiq.veh.isn_backend.dto.response.DefaultResponseDTO;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.RoleRepository;
import athiq.veh.isn_backend.repository.UserRepository;
import athiq.veh.isn_backend.constant.RoleAuthorityEnum;
import athiq.veh.isn_backend.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/roles")
public class RoleController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RoleController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PutMapping("/users/{email}/{roleName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void changeUserRoleToAdmin(@PathVariable String email, @PathVariable String roleName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<Role> optionalRole = roleRepository.findByAuthority(RoleAuthorityEnum.valueOf(roleName));
            if (optionalRole.isPresent()) {
                Role newRole = optionalRole.get();
                user.setRole(newRole);
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Role not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @GetMapping("/users/admins")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllAdminUsers() {
        Optional<Role> adminRole = roleRepository.findByAuthority(RoleAuthorityEnum.ROLE_ADMIN);

        if (adminRole.isPresent()) {
            return userRepository.findByRole(adminRole.get());
        } else {
            throw new IllegalArgumentException("Admin role not found");
        }
    }

    @GetMapping("/users/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUserUsers() {
        Optional<Role> userRole = roleRepository.findByAuthority(RoleAuthorityEnum.ROLE_USER);

        if (userRole.isPresent()) {
            return userRepository.findByRole(userRole.get());
        } else {
            throw new IllegalArgumentException("User role not found");
        }
    }

    @PutMapping("/users/deactivate/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> deactivateUser(@PathVariable String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (Boolean.FALSE.equals(user.getIsActivated())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new DefaultResponseDTO("User is already deactivated.", null));
                }

                user.setIsActivated(Boolean.FALSE);
                userRepository.save(user);

                return ResponseEntity.ok(new DefaultResponseDTO("User has been deactivated successfully.", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DefaultResponseDTO("User not found.", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DefaultResponseDTO("Failed to deactivate user.", null));
        }
    }


    @PutMapping("/users/reactivate/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> reactivateUser(@PathVariable String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (Boolean.TRUE.equals(user.getIsActivated())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new DefaultResponseDTO("User is already active.", null));
                }

                user.setIsActivated(Boolean.TRUE);
                userRepository.save(user);

                return ResponseEntity.ok(new DefaultResponseDTO("User has been reactivated successfully.", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DefaultResponseDTO("User not found.", null));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DefaultResponseDTO("Failed to reactivate user.", null));
        }
    }


    @PutMapping("/users/change-role/{email}/{newRole}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> changeUserRole(@PathVariable String email, @PathVariable String newRole) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                Optional<Role> optionalRole = roleRepository.findByAuthority(RoleAuthorityEnum.valueOf(newRole));
                if (optionalRole.isPresent()) {
                    Role newRoleEntity = optionalRole.get();
                    user.setRole(newRoleEntity);
                    userRepository.save(user);

                    return ResponseEntity.ok(new DefaultResponseDTO("User role changed successfully.", null));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new DefaultResponseDTO("Role not found.", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DefaultResponseDTO("User not found.", null));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DefaultResponseDTO("Failed to change user role.", null));
        }
    }



    @GetMapping("/users/active")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActivatedTrue();
    }

    @GetMapping("/users/deactivated")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllDeactivatedUsers() {
        return userRepository.findByIsActivatedFalse();
    }
}