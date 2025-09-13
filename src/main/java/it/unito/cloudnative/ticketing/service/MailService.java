package it.unito.cloudnative.ticketing.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

            // --- LOG  ---
            log.info("[SendGrid] to={} status={} messageId={}",
                    toEmail, res.getStatusCode(), res.getHeaders().get("X-Message-Id"));
            if (res.getBody() != null && !res.getBody().isBlank()) {
                log.debug("[SendGrid] body={}", res.getBody());
            }
            if (res.getStatusCode() >= 400) {
                log.error("[SendGrid] headers={}", res.getHeaders());
                throw new IllegalStateException(
                        "SendGrid error: " + res.getStatusCode() + " - " + res.getBody());
            }
        }
    }
}
