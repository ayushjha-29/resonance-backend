package com.resonance.resonance.service;

import com.resonance.resonance.entity.AppUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void sendVerificationEmail(AppUser user , String verificationLink) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = createHelper(message ,user , "Verify your Resonance account");

        Context context = new Context();

        context.setVariable("username",user.getUsername());
        context.setVariable("verificationLink" , verificationLink);

        String html = templateEngine.process("verify-account",context);

        helper.setText(html,true);

        mailSender.send(message);

    }

    @Async
    public void sendWelcomeEmail(AppUser user) throws MessagingException{

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = createHelper(message,user,"Welcome to Resonance");

        Context context = new Context();

        context.setVariable("username",user.getUsername());
        context.setVariable("loginLink" , "http://192.168.29.29:8080/auth/login");

        String html = templateEngine.process("welcome",context);

        helper.setText(html,true);

        mailSender.send(message);

    }

    @Async
    public void sendResetPasswordEmail(AppUser user , String resetLink) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = createHelper(message,user,"Password reset");

        Context context = new Context();

        context.setVariable("resetLink" , resetLink);
        context.setVariable("username" , user.getUsername());

        String html = templateEngine.process("reset-password",context);

        helper.setText(html,true);

        mailSender.send(message);

    }

    private MimeMessageHelper createHelper(MimeMessage message , AppUser user , String subject) throws MessagingException {

        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(senderEmail);

        helper.setTo(user.getEmail());

        helper.setSubject(subject);

        return helper;

    }

}
