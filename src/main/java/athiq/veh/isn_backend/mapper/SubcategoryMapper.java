package athiq.veh.isn_backend.mapper;

import athiq.veh.isn_backend.dto.response.SubCategoryResponseDTO;
import athiq.veh.isn_backend.model.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubcategoryMapper {

    SubcategoryMapper INSTANCE = Mappers.getMapper(SubcategoryMapper.class);

    // Mapping SubCategory to SubCategoryResponseDTO
    SubCategoryResponseDTO toSubCategoryResponseDTO(SubCategory subCategory);

    // Optional: Mapping SubCategoryResponseDTO back to SubCategory if needed
    SubCategory toSubCategory(SubCategoryResponseDTO subCategoryResponseDTO);
}
