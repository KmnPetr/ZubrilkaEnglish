package com.example.zeapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String mailFrom;
    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMessage(String userMail, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        Context context = new Context();

        context.setVariable("subject", "Код подтверждения");
        context.setVariable("kod","5965");
        context.setVariable("message", "Ваш код подтверждения email на Zubrilka English.");

        String htmlContent = templateEngine.process("email-template", context);

        helper.setTo(userMail);
        helper.setSubject("Код подтверждения");
        helper.setText(htmlContent, true); // true indicates HTML
        helper.setFrom(mailFrom);

        mailSender.send(mimeMessage);
    }
}