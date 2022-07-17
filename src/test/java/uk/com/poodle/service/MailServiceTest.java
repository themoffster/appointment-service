package uk.com.poodle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import uk.com.poodle.config.ServiceConfig;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender mockMailSender;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ServiceConfig mockServiceConfig;

    @InjectMocks
    private MailService mailService;

    @Test
    void shouldSendAnEMail() throws Exception {
        var message = new MimeMessage((Session) null);
        when(mockMailSender.createMimeMessage()).thenReturn(message);
        when(mockServiceConfig.getMail().getFrom()).thenReturn("from@foo.com");

        mailService.send("to@foo.com", "subject", "<div>foo</div>");
        assertEquals("from@foo.com", message.getFrom()[0].toString());
        assertEquals("to@foo.com", message.getAllRecipients()[0].toString());
        assertEquals("subject", message.getSubject());
        assertEquals("<div>foo</div>", message.getContent());

        verify(mockMailSender).send(message);
    }
}
