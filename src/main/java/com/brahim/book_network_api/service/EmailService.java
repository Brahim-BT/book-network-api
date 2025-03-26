package com.brahim.book_network_api.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.brahim.book_network_api.enums.EmailTemplateName;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String username, EmailTemplateName emailTemplateName, String confirmationUrl,
            String activationToken, String subject) throws MessagingException {
        String templateName;
        if (emailTemplateName == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplateName.name();
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activationToken", activationToken);
        Context context = new Context();
        context.setVariables(properties);
        mimeMessageHelper.setFrom("baybat7@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(templateEngine.process(templateName, context), true);
        javaMailSender.send(mimeMessage);
    }
}
