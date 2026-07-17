package com.example.gchc.service;

import com.example.gchc.entity.CoreValue;
import com.example.gchc.repository.CoreValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoreValueService {
    @Autowired
    private CoreValueRepository repository;

    public List<CoreValue> getAll() {
        return repository.findAll();
    }

    public List<CoreValue> getEnabled() {
        return repository.findByEnabledTrueOrderBySortOrderAsc();
    }

    public CoreValue create(CoreValue value) {
        return repository.save(value);
    }

    public CoreValue update(Long id, CoreValue value) {
        value.setId(id);
        return repository.save(value);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
