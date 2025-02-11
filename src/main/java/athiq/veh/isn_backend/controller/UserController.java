package athiq.veh.isn_backend.controller;

import athiq.veh.isn_backend.dto.auth.UserMinDTO;
import athiq.veh.isn_backend.dto.request.UserUpdateRequestDTO;
import athiq.veh.isn_backend.dto.response.UserResponseDTO;
import athiq.veh.isn_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Set<UserMinDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestPart(name = "data") UserUpdateRequestDTO requestDTO, @RequestPart("file")MultipartFile file) throws IOException {

        return ResponseEntity.ok(this.userService.updateUser(file,requestDTO, id));

    }

    @GetMapping("/search")
    public ResponseEntity<List<UserMinDTO>> searchUsers(@RequestParam("firstName") String firstName) {
        return ResponseEntity.ok(userService.searchUsersByFirstName(firstName));
    }

}

