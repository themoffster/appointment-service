package uk.com.poodle.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uk.com.poodle.config.ServiceConfig;

import javax.mail.Message;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.TEXT_HTML;

@Slf4j
@Service
@RequiredArgsConstructor
class MailService {

    private final JavaMailSender mailSender;
    private final ServiceConfig serviceConfig;

    @SneakyThrows
    public void send(String toAddress, String subject, String htmlContent) {
        log.debug("Sending email to {}.", toAddress);
        var message = mailSender.createMimeMessage();
        message.setFrom(serviceConfig.getMail().getFrom());
        message.setRecipients(Message.RecipientType.TO, toAddress);
        message.setSubject(subject);
        message.setText(htmlContent, StandardCharsets.UTF_8.name(), TEXT_HTML.getSubtype());

        mailSender.send(message);
    }
}
