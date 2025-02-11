package athiq.veh.isn_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import athiq.veh.isn_backend.model.FileBlob;

import java.util.Set;

public record GetWallItemResponseDTO(
        FileBlob profileImageBlob,
        String firstName,
        String lastName,
        String title,
        String description,
        @JsonProperty("blobs")
        Set<BlobResponseDTO> blobSet
) {
}
