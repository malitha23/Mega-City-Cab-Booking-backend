package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.dto.auth.UserMinDTO;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Integer id,
        String name,
        UserMinDTO createdBy,
        UserMinDTO lasModifiedBy,
        LocalDateTime createdDateTime,
        LocalDateTime lastModifiedDateTIme

) {
}
