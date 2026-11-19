package com.example.ems.repository;

import com.example.ems.entity.AdminDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminDetail, Long> {
    Optional<AdminDetail> findByEmailIgnoreCase(String email);
}
