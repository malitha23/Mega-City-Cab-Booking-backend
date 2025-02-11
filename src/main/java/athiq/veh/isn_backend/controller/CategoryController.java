package athiq.veh.isn_backend.controller;

import athiq.veh.isn_backend.dto.request.CategoryNameUpdateRequestDTO;
import athiq.veh.isn_backend.dto.response.CategoryResponseDTO;
import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import athiq.veh.isn_backend.model.Category;
import athiq.veh.isn_backend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Integer id, @RequestBody CategoryNameUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.updateCategoryName(requestDTO, id));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
//        this.categoryService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}