package com.voicemail.restapi.service;

import com.voicemail.restapi.model.Email;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.EmailRepository;
import com.voicemail.restapi.repository.UserRepository;
import com.voicemail.restapi.util.MailInfo;
import lombok.RequiredArgsConstructor;
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

    public List<Email> getAllReceivedEmailsForCurrentUser(){
        User currentUser = userAuthenticationService.getCurrentUser();
        return emailRepository.findAllByToEmail(currentUser.getEmail());
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
        return emailRepository.save(email);
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
