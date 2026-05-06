package com.app.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;


@RequiredArgsConstructor
@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.email.from}")
    private String fromEmail;

    public void sendResetEmail(String toEmail, String token) {

        try {
            String resetLink = frontendUrl + "/reset-password?token=" + token;

            Email from = new Email(fromEmail);
            String subject = "Reset Your Password";
            Email to = new Email(toEmail);

            Content content = new Content(
                    "text/plain",
                    "Click the link to reset your password:\n\n" + resetLink
            );

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