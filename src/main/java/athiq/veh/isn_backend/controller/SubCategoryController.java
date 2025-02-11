package athiq.veh.isn_backend.controller;

import athiq.veh.isn_backend.model.SubCategory;
import athiq.veh.isn_backend.service.SubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @GetMapping
    public List<SubCategory> getAllSubCategories() {
        return subCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable("id") Integer id) {
        Optional<SubCategory> subCategory = subCategoryService.findById(id);
        return subCategory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        return subCategoryService.findByCategoryId(categoryId);
    }

    @PostMapping
    public SubCategory addSubCategory(@RequestBody SubCategory subCategory) {
        return subCategoryService.save(subCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> updateSubCategory(@PathVariable("id") Integer id, @RequestBody SubCategory subCategoryDetails) {
        Optional<SubCategory> subCategory = subCategoryService.findById(id);

        if (subCategory.isPresent()) {
            SubCategory existingSubCategory = subCategory.get();
            existingSubCategory.setName(subCategoryDetails.getName());
            existingSubCategory.setCategory(subCategoryDetails.getCategory());
            SubCategory updatedSubCategory = subCategoryService.update(existingSubCategory);
            return ResponseEntity.ok(updatedSubCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable("id") Integer id) {
        if (subCategoryService.findById(id).isPresent()) {
            subCategoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

