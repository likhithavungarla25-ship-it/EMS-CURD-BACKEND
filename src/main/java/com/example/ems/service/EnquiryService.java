package com.example.ems.service;

import com.example.ems.dto.EnquiryRequest;
import com.example.ems.dto.EnquiryResponse;
import com.example.ems.entity.Enquiry;
import com.example.ems.enums.EnquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnquiryService {
    EnquiryResponse createEnquiry(EnquiryRequest request);

    Page<EnquiryResponse> getAllEnquiries(Pageable pageable);

    List<Enquiry> getClosedEnquiry();

    EnquiryResponse updateEnquiry(Long id,EnquiryRequest request);


    EnquiryResponse updateEnquiryStatus(
            Long id,
            EnquiryStatus status
    );

    void deleteEnquiry(Long id);
}
