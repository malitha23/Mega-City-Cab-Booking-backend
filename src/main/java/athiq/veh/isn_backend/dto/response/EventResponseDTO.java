package athiq.veh.isn_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record EventResponseDTO(
        String title,
        String description,
        String imageUrl
) {
}
