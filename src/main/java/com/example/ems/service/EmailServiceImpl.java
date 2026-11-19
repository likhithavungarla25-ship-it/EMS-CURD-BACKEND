package com.example.ems.service;


import com.example.ems.event.EnquiryStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        name = "email.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class EmailServiceImpl implements EmailService {
    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${aivra.mail.from}") String fromEmail
    ) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        log.info("EmailServiceImpl initialized successfully. From email: {}", fromEmail);
    }

    @Override
    public void sendEnquiryStatusChangeNotification(EnquiryStatusChangedEvent event) {
        try {
            log.info("Attempting to send email to: {}", event.recipientEmail());
            log.info("Email configuration - From: {}, Host: {}, Port: {}", fromEmail, mailSender.getClass().getSimpleName());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(event.recipientEmail());
            message.setSubject("Your Enquiry Status Has Been Updated");

            String emailBody = buildEmailBody(event);
            message.setText(emailBody);

            mailSender.send(message);

            log.info(
                    "Successfully sent enquiry status change email. EnquiryID={}, Recipient={}",
                    event.enquiryId(),
                    event.recipientEmail()
            );

        } catch (Exception ex) {
            log.error(
                    "Failed to send enquiry status change email. EnquiryID={}, Recipient={}",
                    event.enquiryId(),
                    event.recipientEmail(),
                    ex
            );
        }
    }

    private String buildEmailBody(EnquiryStatusChangedEvent event) {
        return String.format(
                "Dear User,\n\n" +
                "Your enquiry (ID: %s) status has been updated.\n\n" +
                "Previous Status: %s\n" +
                "New Status: %s\n" +
                "Changed At: %s\n\n" +
                "If you have any questions, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Aivra Team",
                event.enquiryId(),
                event.oldStatus(),
                event.newStatus(),
                event.changedAt()
        );
    }
}
