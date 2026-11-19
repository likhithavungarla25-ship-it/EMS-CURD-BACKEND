package com.example.ems.dto;

import com.example.ems.enums.EnquiryStatus;
import jakarta.validation.constraints.NotNull;

public class EnquiryStatusRequest {

    @NotNull(message = "Status is required")
    private EnquiryStatus status;

    public EnquiryStatusRequest() {}

    public EnquiryStatusRequest(EnquiryStatus status) {
        this.status = status;
    }

    public EnquiryStatus getStatus() {
        return status;
    }

    public void setStatus(EnquiryStatus status) {
        this.status = status;
    }
}
