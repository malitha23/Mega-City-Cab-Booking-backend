package athiq.veh.isn_backend.mapper;

import athiq.veh.isn_backend.dto.response.BlobResponseDTO;
import athiq.veh.isn_backend.model.FileBlob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlobMapper {

    BlobMapper INSTANCE = Mappers.getMapper(BlobMapper.class);

    // Map to User
    @Mapping(target = "uuid" , source = "uuid")
    @Mapping(target = "id" , source = "id")
    BlobResponseDTO toDto(FileBlob fileBlob);

    // User to Map

    FileBlob toBlob(BlobResponseDTO blobResponseDTO);

}
