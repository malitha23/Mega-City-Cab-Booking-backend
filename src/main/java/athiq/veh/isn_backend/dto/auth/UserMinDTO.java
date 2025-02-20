package athiq.veh.isn_backend.dto.auth;

import athiq.veh.isn_backend.model.FileBlob;

public record UserMinDTO(
        Long id,
        String email,
        String firstName,
        
        String lastName,
        String imageUrl,
        FileBlob imageBlob,
        String role
) {
}
