//package com.jxg.isn_backend.service;
//
//import com.jxg.isn_backend.model.SubCategory;
//import com.jxg.isn_backend.repository.SubCategoryRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class SubCategoryService {
//
//    private final SubCategoryRepository subCategoryRepository;
//
//    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
//        this.subCategoryRepository = subCategoryRepository;
//    }
//
//    public List<SubCategory> findAll() {
//        return subCategoryRepository.findAll();
//    }
//
//    public Optional<SubCategory> findById(Integer id) {
//        return subCategoryRepository.findById(id);
//    }
//
//    public List<SubCategory> findByCategoryId(Integer categoryId) {
//        return subCategoryRepository.findByCategoryId(categoryId);
//    }
//
//    public SubCategory save(SubCategory subCategory) {
//        return subCategoryRepository.save(subCategory);
//    }
//
//    public SubCategory update(SubCategory subCategory) {
//        return subCategoryRepository.save(subCategory);
//    }
//
//    public void deleteById(Integer id) {
//        subCategoryRepository.deleteById(id);
//    }
//}


package athiq.veh.isn_backend.service;
import org.springframework.transaction.annotation.Transactional;
import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import athiq.veh.isn_backend.model.SubCategory;
import athiq.veh.isn_backend.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    public List<SubCategory> findAll() {
        return subCategoryRepository.findAll();
    }

    public Optional<SubCategory> findById(Integer id) {
        return subCategoryRepository.findById(id);
    }

    public List<SubCategory> findByCategoryId(Integer categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    public SubCategory save(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public SubCategory update(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!subCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("SubCategory not found");
        }
        subCategoryRepository.deleteById(id);
    }
}
