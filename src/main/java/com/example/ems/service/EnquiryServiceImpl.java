package com.example.ems.service;


import com.example.ems.dto.EnquiryRequest;
import com.example.ems.dto.EnquiryResponse;
import com.example.ems.entity.Enquiry;
import com.example.ems.enums.EnquiryStatus;
import com.example.ems.event.EnquiryStatusChangedEvent;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.kafka.EnquiryEventProducer;
import com.example.ems.repository.EnquiryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private static final Logger log = LoggerFactory.getLogger(EnquiryServiceImpl.class);

    private final EnquiryRepository enquiryRepository;

    @Autowired(required = false)
    private EnquiryEventProducer enquiryEventProducer;

    @Autowired(required = false)
    private EmailService emailService;

    @Autowired
    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        Enquiry enquiry = new Enquiry();


        enquiry.setName(request.getName().trim());
        enquiry.setEmail(request.getEmail().trim());
        enquiry.setMessage(request.getMessage().trim());
        enquiry.setPhone(request.getPhone().trim());
        enquiry.setStatus(EnquiryStatus.NEW);
        enquiry.setCity(request.getCity().trim());

        Enquiry saved = enquiryRepository.save(enquiry);

        return toResponse(saved);

    }

    @Override
    public List<Enquiry> getClosedEnquiry(){
        return enquiryRepository.findByStatus(EnquiryStatus.CLOSED);
    }


    @Override
    public Page<EnquiryResponse> getAllEnquiries(Pageable pageable) {
        return enquiryRepository.findAll(pageable).map(this::toResponse);
    }



    @Override
    public EnquiryResponse updateEnquiry(Long id,EnquiryRequest enquiryRequest){
        Enquiry enquiry = findEnquiryOrThrow(id);
        enquiry.setName(enquiryRequest.getName().trim());
        enquiry.setEmail(enquiryRequest.getEmail().trim());
        enquiry.setMessage(enquiryRequest.getMessage().trim());
        enquiry.setPhone(enquiryRequest.getPhone().trim());
        enquiry.setCity(enquiryRequest.getCity().trim());

        enquiryRepository.save(enquiry);
        return toResponse(enquiry);
    }

    @Override
    @Transactional
    public EnquiryResponse updateEnquiryStatus(
            Long id,
            EnquiryStatus status) {

        Enquiry enquiry = findEnquiryOrThrow(id);
        EnquiryStatus oldStatus = enquiry.getStatus();
        enquiry.setStatus(status);

        Enquiry updated = enquiryRepository.save(enquiry);

        EnquiryStatusChangedEvent event = new EnquiryStatusChangedEvent(
                updated.getId().toString(),
                updated.getEmail(),
                oldStatus.toString(),
                status.toString(),
                Instant.now()
        );

        // Try to publish via Kafka if enabled
        if (enquiryEventProducer != null) {
            enquiryEventProducer.publish(event);
        }

        // Send email directly if email service is available (bypass Kafka)
        if (emailService != null) {
            log.info("EmailService is available, sending email directly for enquiry ID={}", updated.getId());
            emailService.sendEnquiryStatusChangeNotification(event);
        } else {
            log.warn("EmailService is not available - email.enabled may be false");
        }

        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteEnquiry(Long id) {
        Enquiry enquiry = findEnquiryOrThrow(id);
        enquiryRepository.delete(enquiry);
    }

    private Enquiry findEnquiryOrThrow(Long id) {
        return enquiryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enquiry not found with ID: " + id
                        )
                );
    }


    private EnquiryResponse toResponse(Enquiry enquiry) {
        return new EnquiryResponse(
                enquiry.getId(),
                enquiry.getName(),
                enquiry.getEmail(),
                enquiry.getPhone(),
                enquiry.getMessage(),
                enquiry.getCity(),
                enquiry.getStatus(),
                enquiry.getCreatedAt()
        );
    }

}