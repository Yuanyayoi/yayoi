
package com.example.gchc.service;

import com.example.gchc.dto.CategoryDTO;
import com.example.gchc.entity.Category;
import com.example.gchc.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryDTO::fromEntity).toList();
    }

    public List<CategoryDTO> getParentCategories() {
        return categoryRepository.findByParentIdIsNullAndEnabledTrueOrderBySortOrderAsc()
                .stream().map(CategoryDTO::fromEntity).toList();
    }

    public List<CategoryDTO> getChildCategories(Long parentId) {
        return categoryRepository.findByParentIdAndEnabledTrueOrderBySortOrderAsc(parentId)
                .stream().map(CategoryDTO::fromEntity).toList();
    }

    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(CategoryDTO::fromEntity);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setParentId(categoryDTO.getParentId());
        category.setSortOrder(categoryDTO.getSortOrder() != null ? categoryDTO.getSortOrder() : 0);
        category.setEnabled(categoryDTO.getEnabled() != null ? categoryDTO.getEnabled() : true);
        return CategoryDTO.fromEntity(categoryRepository.save(category));
    }

    public Optional<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id).map(category -> {
            if (categoryDTO.getName() != null) category.setName(categoryDTO.getName());
            if (categoryDTO.getDescription() != null) category.setDescription(categoryDTO.getDescription());
            if (categoryDTO.getParentId() != null) category.setParentId(categoryDTO.getParentId());
            if (categoryDTO.getSortOrder() != null) category.setSortOrder(categoryDTO.getSortOrder());
            if (categoryDTO.getEnabled() != null) category.setEnabled(categoryDTO.getEnabled());
            return CategoryDTO.fromEntity(categoryRepository.save(category));
        });
    }

    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
