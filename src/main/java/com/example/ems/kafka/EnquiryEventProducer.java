package com.example.ems.kafka;

import com.example.ems.event.EnquiryStatusChangedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



@Service
@ConditionalOnProperty(
        name = "kafka.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class EnquiryEventProducer {
    private static final Logger log =
            LoggerFactory.getLogger(EnquiryEventProducer.class);

    private static final String TOPIC =
            "enquiry-status-changed";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public EnquiryEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper

    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(EnquiryStatusChangedEvent event){
        final String json;
        try{
            json = objectMapper.writeValueAsString(event);

        }catch (JsonProcessingException ex){
            throw new IllegalStateException(
                    "Could not serialize enquiry status event", ex);
        }

        kafkaTemplate.send(TOPIC, event.enquiryId(), json)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error(
                                "Failed to publish enquiry status event. ID={}",
                                event.enquiryId(), error);
                    } else {
                        log.info(
                                "Published enquiry status event. ID={}, partition={}",
                                event.enquiryId(),
                                result.getRecordMetadata().partition());
                    }
                });
    }

}
