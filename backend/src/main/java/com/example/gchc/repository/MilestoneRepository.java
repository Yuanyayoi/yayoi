package com.example.gchc.repository;

import com.example.gchc.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByEnabledTrueOrderBySortOrderAsc();
}
