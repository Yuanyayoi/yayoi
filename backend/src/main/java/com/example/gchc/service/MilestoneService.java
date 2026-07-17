package com.example.gchc.service;

import com.example.gchc.entity.Milestone;
import com.example.gchc.repository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilestoneService {
    @Autowired
    private MilestoneRepository repository;

    public List<Milestone> getAll() {
        return repository.findAll();
    }

    public List<Milestone> getEnabled() {
        return repository.findByEnabledTrueOrderBySortOrderAsc();
    }

    public Milestone create(Milestone milestone) {
        return repository.save(milestone);
    }

    public Milestone update(Long id, Milestone milestone) {
        milestone.setId(id);
        return repository.save(milestone);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
