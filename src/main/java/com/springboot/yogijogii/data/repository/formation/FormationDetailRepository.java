package com.springboot.yogijogii.data.repository.formation;

import com.springboot.yogijogii.data.entity.FormationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormationDetailRepository extends JpaRepository<FormationDetail, Long> {
    List<FormationDetail> findByFormationId(Long id);
}
