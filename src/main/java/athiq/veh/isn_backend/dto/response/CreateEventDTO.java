package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.dto.auth.UserMinDTO;

import java.time.LocalDateTime;

public record CreateEventDTO(

        BlobResponseDTO imageBlob,
        UserMinDTO createdBy,
        UserMinDTO lastModifiedBy,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt

) {
}