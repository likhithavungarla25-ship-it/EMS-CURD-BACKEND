package com.example.ems.controller;


import com.example.ems.dto.EnquiryRequest;
import com.example.ems.dto.EnquiryResponse;
import com.example.ems.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enquiry")
public class EnquiryController {
    private final EnquiryService enquiryService;

    EnquiryController(EnquiryService enquiryService){
        this.enquiryService = enquiryService;
    }

    //Create enquiry
    @PostMapping
    public ResponseEntity<EnquiryResponse> createEnquiry(@Valid @RequestBody EnquiryRequest enquiryRequest){
        EnquiryResponse  response = enquiryService.createEnquiry(enquiryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }






}
