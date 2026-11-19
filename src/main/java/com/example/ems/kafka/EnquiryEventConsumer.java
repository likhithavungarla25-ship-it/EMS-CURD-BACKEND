package com.example.ems.kafka;


import com.example.ems.event.EnquiryStatusChangedEvent;
import com.example.ems.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        name = "kafka.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class EnquiryEventConsumer {
    private static final Logger log =
            LoggerFactory.getLogger(EnquiryEventConsumer.class);

    private final ObjectMapper objectMapper;

    @Autowired(required = false)
    private EmailService emailService;

    @Autowired
    public EnquiryEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "enquiry-status-changed",
            groupId = "aivra-enquiry-email"
    )
    public void consumeEnquiryStatusChangedEvent(String message) {
        try {
            EnquiryStatusChangedEvent event = objectMapper.readValue(
                    message,
                    EnquiryStatusChangedEvent.class
            );

            log.info(
                    "Received enquiry status change event. ID={}, Email={}, OldStatus={}, NewStatus={}",
                    event.enquiryId(),
                    event.recipientEmail(),
                    event.oldStatus(),
                    event.newStatus()
            );

            if (emailService != null) {
                emailService.sendEnquiryStatusChangeNotification(event);
            } else {
                log.warn("EmailService is not available. Skipping email notification for enquiry ID={}", event.enquiryId());
            }

        } catch (Exception ex) {
            log.error(
                    "Failed to process enquiry status change event. Message={}",
                    message,
                    ex
            );
        }
    }
}
