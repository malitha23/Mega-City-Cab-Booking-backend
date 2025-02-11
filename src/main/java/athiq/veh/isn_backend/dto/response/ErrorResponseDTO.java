package athiq.veh.isn_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import athiq.veh.isn_backend.constant.ResponseStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponseDTO(
        Integer statusCode,

        @Enumerated(EnumType.STRING)
        ResponseStatusEnum status,
        LocalDateTime timeStamp,
        String message,
        String description
) {
}
