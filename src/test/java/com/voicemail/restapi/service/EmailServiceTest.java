package com.voicemail.restapi.service;

import com.voicemail.restapi.model.Email;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.EmailRepository;
import com.voicemail.restapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private EmailRepository emailRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserAuthenticationService userAuthenticationServiceMock;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testGetAllReceivedEmailsForCurrentUser() {
        User currentUser = new User();
        currentUser.setEmail("testuser@example.com");
        Mockito.when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(currentUser);
        List<Email> expectedEmails = Arrays.asList(
                new Email(1l,currentUser.getEmail(), "sender1@example.com", "Subject 1", "Body 1", LocalDateTime.now(),null),
                new Email(2l,currentUser.getEmail(), "sender2@example.com", "Subject 2", "Body 2", LocalDateTime.now(), null)
        );
        Mockito.when(emailRepositoryMock.findAllByToEmail(currentUser.getEmail())).thenReturn(expectedEmails);

        List<Email> actualEmails = emailService.getAllReceivedEmailsForCurrentUser();

        assertEquals(expectedEmails, actualEmails);
        Mockito.verify(userAuthenticationServiceMock, Mockito.times(1)).getCurrentUser();
        Mockito.verify(emailRepositoryMock, Mockito.times(1)).findAllByToEmail(currentUser.getEmail());
    }

    @Test
    public void testGetAllSendEmailsForCurrentUser() {
        User currentUser = new User();
        currentUser.setEmail("testuser@example.com");
        Mockito.when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(currentUser);
        List<Email> expectedEmails = Arrays.asList(
                new Email(1l,currentUser.getEmail(), "sender1@example.com", "Subject 1", "Body 1", LocalDateTime.now(),null),
                new Email(2l,currentUser.getEmail(), "sender2@example.com", "Subject 2", "Body 2", LocalDateTime.now(), null)
        );
        Mockito.when(emailRepositoryMock.findAllByFromEmail(currentUser.getEmail())).thenReturn(expectedEmails);

        List<Email> actualEmails = emailService.getAllSendEmailsForCurrentUser();

        assertEquals(expectedEmails, actualEmails);
        Mockito.verify(userAuthenticationServiceMock, Mockito.times(1)).getCurrentUser();
        Mockito.verify(emailRepositoryMock, Mockito.times(1)).findAllByFromEmail(currentUser.getEmail());
    }


    @Test
    public void testGetEmailById() {
        Email expectedEmail = new Email(1l, "recipient@example.com", "sender@example.com", "Subject", "Body", LocalDateTime.now(),null);
        Mockito.when(emailRepositoryMock.findById(expectedEmail.getId())).thenReturn(Optional.of(expectedEmail));

        Email actualEmail = emailService.getEmailById(expectedEmail.getId());

        assertEquals(expectedEmail, actualEmail);
        Mockito.verify(emailRepositoryMock, Mockito.times(1)).findById(expectedEmail.getId());
    }

}
