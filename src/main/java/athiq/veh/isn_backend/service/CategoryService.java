package athiq.veh.isn_backend.service;
import org.springframework.transaction.annotation.Transactional;

import athiq.veh.isn_backend.dto.request.CategoryNameUpdateRequestDTO;
import athiq.veh.isn_backend.dto.response.CategoryResponseDTO;
import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import athiq.veh.isn_backend.mapper.UserMapper;
import athiq.veh.isn_backend.model.Category;
import athiq.veh.isn_backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public CategoryResponseDTO updateCategoryName(CategoryNameUpdateRequestDTO requestDTO, Integer id) {

        UserMapper userMapper = UserMapper.INSTANCE;
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category category;
        if (optionalCategory.isPresent()) {
            category = optionalCategory.get();
            category.setName(requestDTO.name());
        } else {
            throw new ResourceNotFoundException("category not found");
        }

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponseDTO(
                updatedCategory.getId(),
                updatedCategory.getName(),
                userMapper.toUserMinDTO(updatedCategory.getCreatedBy()),
                userMapper.toUserMinDTO(updatedCategory.getLastModifiedBy()),
                updatedCategory.getCreatedDatetime(),
                updatedCategory.getLastModifiedDatetime()
        );
    }


//    public void deleteById(Integer id) {
//
//        if (categoryRepository.findById(id).isPresent()) {
//            categoryRepository.deleteById(id);
//        } else {
//            throw new ResourceNotFoundException("category not found");
//        }
//
//
//    }

    @Transactional
    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

}

