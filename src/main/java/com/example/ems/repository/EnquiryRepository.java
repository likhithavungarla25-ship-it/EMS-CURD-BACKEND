package com.example.ems.repository;


import com.example.ems.entity.Enquiry;
import com.example.ems.enums.EnquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    //Always use Respositry method name such as findByStatus, findByEmail, findByNumber
    List<Enquiry> findByStatus(EnquiryStatus status);
}
