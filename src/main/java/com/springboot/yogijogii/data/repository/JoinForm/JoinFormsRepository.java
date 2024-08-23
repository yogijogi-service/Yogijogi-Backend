package com.springboot.yogijogii.data.repository.JoinForm;


import com.springboot.yogijogii.data.entity.JoinForms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinFormsRepository extends JpaRepository<JoinForms,Long>, JoinFormsCustom {

}
