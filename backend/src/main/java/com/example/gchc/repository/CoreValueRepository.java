package com.example.gchc.repository;

import com.example.gchc.entity.CoreValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreValueRepository extends JpaRepository<CoreValue, Long> {
    List<CoreValue> findByEnabledTrueOrderBySortOrderAsc();
}
