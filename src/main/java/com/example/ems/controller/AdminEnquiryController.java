package com.example.ems.controller;


import com.example.ems.dto.EnquiryRequest;
import com.example.ems.dto.EnquiryResponse;
import com.example.ems.dto.EnquiryStatusRequest;
import com.example.ems.entity.Enquiry;
import com.example.ems.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/enquiries")
public class AdminEnquiryController {
    private final EnquiryService enquiryService;

    AdminEnquiryController (EnquiryService enquiryService){
        this.enquiryService = enquiryService;
    }


    //Get all enquiries
    @GetMapping
    public ResponseEntity<Page<EnquiryResponse>> getAllEnquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page must be non-negative and size must be between 1 and 100"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return ResponseEntity.ok(
                enquiryService.getAllEnquiries(pageable)
        );
    }


    //Get CLOSED enquiries
    @GetMapping("/closed")
    public List<Enquiry> getClosedEnquiry(){
        return enquiryService.getClosedEnquiry();
    }

    //Update enquiry
    @PutMapping("/{id}")
    public ResponseEntity<EnquiryResponse> updateEnquiry(
            @PathVariable Long id,
            @Valid @RequestBody EnquiryRequest request) {

        EnquiryResponse updatedEnquiry = enquiryService.updateEnquiry(id, request);

        return ResponseEntity.ok(updatedEnquiry);
    }



    // Change enquiry status
    @PatchMapping("/{id}/status")
    public ResponseEntity<EnquiryResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody EnquiryStatusRequest request) {

        return ResponseEntity.ok(
                enquiryService.updateEnquiryStatus(
                        id, request.getStatus()
                )
        );
    }

    // Delete an enquiry
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEnquiry(
            @PathVariable Long id) {

        enquiryService.deleteEnquiry(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Enquiry deleted successfully");

        return ResponseEntity.ok(response);
    }
}
