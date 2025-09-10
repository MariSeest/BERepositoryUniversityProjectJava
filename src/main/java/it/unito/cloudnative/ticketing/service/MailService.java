// src/main/java/it/unito/cloudnative/ticketing/service/MailService.java
package it.unito.cloudnative.ticketing.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${sendgrid.apiKey}")
    private String apiKey;

    @Value("${sendgrid.from}")
    private String fromEmail;

    @Value("${sendgrid.fromName}")
    private String fromName;

    @Value("${sendgrid.to}")
    private List<String> toEmails;

    /**
     * Invia una mail HTML a tutti i destinatari configurati.
     * Logga status/body/headers della risposta SendGrid per facilitare il debug.
     */
    public void sendSimple(String subject, String htmlBody) throws Exception {
        Email from = new Email(fromEmail, fromName);
        Content content = new Content("text/html", htmlBody);

        SendGrid sg = new SendGrid(apiKey);

        for (String toEmail : toEmails) {
            Email to = new Email(toEmail);
            Mail mail = new Mail(from, subject, to, content);

            Request req = new Request();
            req.setMethod(Method.POST);
            req.setEndpoint("mail/send");
            req.setBody(mail.build());

            Response res = sg.api(req);

            // --- LOG DETTAGLIATO ---
            System.out.println("[SendGrid] To=" + toEmail);
            System.out.println("[SendGrid] Status=" + res.getStatusCode());
            System.out.println("[SendGrid] Body=" + res.getBody());
            System.out.println("[SendGrid] Headers=" + res.getHeaders());

            if (res.getStatusCode() >= 400) {
                throw new IllegalStateException(
                        "SendGrid error: " + res.getStatusCode() + " - " + res.getBody());
            }
        }
    }
}
