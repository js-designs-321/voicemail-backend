package com.voicemail.restapi.controller;

import com.voicemail.restapi.model.Email;
import com.voicemail.restapi.service.EmailService;
import com.voicemail.restapi.util.MailInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @Test
    public void testGetAllReceivedEmails() {
        List<Email> emails = new ArrayList<>();
        emails.add(new Email());
        emails.add(new Email());

        Mockito.when(emailService.getAllReceivedEmailsForCurrentUser()).thenReturn(emails);

        ResponseEntity<List<Email>> response = emailController.getAllReceivedEmails();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emails.size(), response.getBody().size());
    }

    @Test
    public void testGetAllSentEmails() {
        List<Email> emails = new ArrayList<>();
        emails.add(new Email());
        emails.add(new Email());

        Mockito.when(emailService.getAllSendEmailsForCurrentUser()).thenReturn(emails);

        ResponseEntity<List<Email>> response = emailController.getAllSentEmails();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emails.size(), response.getBody().size());
    }

    @Test
    public void testSendEmail() {
        MailInfo mailInfo = new MailInfo();
        Email email = new Email();

        Mockito.when(emailService.sendEmail(mailInfo)).thenReturn(email);

        ResponseEntity<Email> response = emailController.sendEmail(mailInfo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, response.getBody());
    }

    @Test
    public void testGetEmailById() {
        Long emailId = 1L;
        Email email = new Email();

        Mockito.when(emailService.getEmailById(emailId)).thenReturn(email);

        ResponseEntity<Email> response = emailController.getEmailById(emailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, response.getBody());
    }

    @Test
    public void testDeleteEmailById() {
        Long emailId = 1L;

        ResponseEntity<String> response = emailController.deleteEmailById(emailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email Successfully Deleted", response.getBody());

        Mockito.verify(emailService).deleteEmailById(emailId);
    }
}
