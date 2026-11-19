package com.example.ems.service;


import com.example.ems.event.EnquiryStatusChangedEvent;

public interface EmailService {
    void sendEnquiryStatusChangeNotification(EnquiryStatusChangedEvent event);
}
