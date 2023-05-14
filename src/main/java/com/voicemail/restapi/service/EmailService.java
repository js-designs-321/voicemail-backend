package com.voicemail.restapi.service;

import com.voicemail.restapi.model.Email;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.EmailRepository;
import com.voicemail.restapi.repository.UserRepository;
import com.voicemail.restapi.util.MailInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    private final UserRepository userRepository;

    private final UserAuthenticationService userAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public List<Email> getAllReceivedEmailsForCurrentUser(){
        User currentUser = userAuthenticationService.getCurrentUser();
        List<Email> emails = emailRepository.findAllByToEmail(currentUser.getEmail());
        LOGGER.debug("All emails for current user are fetched successfully");
        return emails;
    }

    public List<Email> getAllSendEmailsForCurrentUser(){
        User currentUser = userAuthenticationService.getCurrentUser();
        return emailRepository.findAllByFromEmail(currentUser.getEmail());
    }

    public Email sendEmail(MailInfo mailInfo){
        User sender = userAuthenticationService.getCurrentUser();
        User receiver = userRepository.findByEmail(mailInfo.getTo()).orElseThrow(() -> new UsernameNotFoundException("Invalid email address"));
        var email = Email.builder()
                .toEmail(receiver.getEmail())
                .fromEmail(sender.getEmail())
                .subject(mailInfo.getSubject())
                .body(mailInfo.getBody())
                .createdAt(LocalDateTime.now())
                .attachment(mailInfo.getAttachment())
                .build();
        Email savedEmail = emailRepository.save(email);
        LOGGER.debug("Email Successfully sent");
        return savedEmail;
    }

    public Email getEmailById(Long id){
        Email email = emailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Email not found"));
        return email;
    }

    public void deleteEmailById(Long id){
        Email email = emailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Email not found"));
        emailRepository.delete(email);
    }

}
