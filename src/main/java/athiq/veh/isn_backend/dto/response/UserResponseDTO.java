package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.model.Role;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        BlobResponseDTO imageBlob,
        Role role
) {
}
