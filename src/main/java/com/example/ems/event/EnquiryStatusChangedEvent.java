package com.example.ems.event;

import java.time.Instant;

public record EnquiryStatusChangedEvent(
        String enquiryId,
        String recipientEmail,
        String oldStatus,
        String newStatus,
        Instant changedAt
) {
}
