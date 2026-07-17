package com.example.gchc.service;

import com.example.gchc.dto.NewsCategoryDTO;
import com.example.gchc.entity.NewsCategory;
import com.example.gchc.repository.NewsCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsCategoryService {

    private final NewsCategoryRepository repository;

    public NewsCategoryService(NewsCategoryRepository repository) {
        this.repository = repository;
    }

    public List<NewsCategoryDTO> getAllCategories() {
        return repository.findAll().stream()
                .map(NewsCategoryDTO::fromEntity)
                .toList();
    }

    public List<NewsCategoryDTO> getEnabledCategories() {
        return repository.findAllEnabled().stream()
                .map(NewsCategoryDTO::fromEntity)
                .toList();
    }

    public Optional<NewsCategoryDTO> getCategoryById(Long id) {
        return repository.findById(id).map(NewsCategoryDTO::fromEntity);
    }

    public NewsCategoryDTO createCategory(NewsCategoryDTO dto) {
        return NewsCategoryDTO.fromEntity(repository.save(dto.toEntity()));
    }

    public Optional<NewsCategoryDTO> updateCategory(Long id, NewsCategoryDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setSortOrder(dto.getSortOrder());
            existing.setEnabled(dto.getEnabled());
            return NewsCategoryDTO.fromEntity(repository.save(existing));
        });
    }

    public boolean deleteCategory(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
