package athiq.veh.isn_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record DefaultResponseDTO(

        String message,
        String Error
) {
}
