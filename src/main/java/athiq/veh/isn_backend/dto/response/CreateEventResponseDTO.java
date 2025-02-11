package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.dto.auth.UserMinDTO;

import java.time.LocalDateTime;
import java.util.Set;


public record CreateEventResponseDTO(
        Long id,
        String title,
        String description,
        BlobResponseDTO imageBlob,
        UserMinDTO createdBy,
        UserMinDTO lasModifiedBy,
        LocalDateTime createdDateTime,
        LocalDateTime lastModifiedDateTIme
) {
}
