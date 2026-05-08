package com.app.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.frontend.reset-url}")
    private String resetUrl;

    @Value("${app.frontend.setup-url}")
    private String setupUrl;

    public void sendForgotPasswordEmail(String toEmail, String token) {

        String link = resetUrl + "?token=" + token;

        String subject = "Reset Your Password";

        String body = """
                We received a request to reset your password.

                Click the link below to reset it:

                %s

                This link expires in 24 hours.
                """.formatted(link);

        sendEmail(toEmail, subject, body);
    }

    public void sendStaffWelcomeEmail(String toEmail, String token) {

        String link = setupUrl + "?token=" + token;

        String subject = "Reset Password Please";

        String body = """
                Welcome to Clinic .

                Your account has been created  by admin.

                Click the link below to setup/change your password:

                %s

                
                This link expires in 24 hours.
                """.formatted(link);

        sendEmail(toEmail, subject, body);
    }

    private void sendEmail(String toEmail, String subject, String body) {

        try {

            Email from = new Email(fromEmail);
            Email to = new Email(toEmail);

            Content content = new Content("text/plain", body);

            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(apiKey);

            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("SendGrid Status: " + response.getStatusCode());

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed");
        }
    }
}