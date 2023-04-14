package com.voicemail.restapi.service;

import com.voicemail.restapi.model.Email;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.EmailRepository;
import com.voicemail.restapi.repository.UserRepository;
import com.voicemail.restapi.util.MailSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    private final UserRepository userRepository;

    private final UserAuthenticationService userService;

    public List<Email> getAllReceivedEmailsForCurrentUser(){
        User currentUser = userService.getCurrentUser();
        return emailRepository.findAllByToEmail(currentUser.getEmail());
    }

    public List<Email> getAllSendEmailsForCurrentUser(){
        User currentUser = userService.getCurrentUser();
        return emailRepository.findAllByFromEmail(currentUser.getEmail());
    }

    public Email sendEmail(MailSendRequest request){
        User sender = userService.getCurrentUser();
        User receiver = userRepository.findByEmail(request.getTo()).orElseThrow(() -> new UsernameNotFoundException("Invalid email address"));
        var email = Email.builder()
                .toEmail(receiver.getEmail())
                .fromEmail(sender.getEmail())
                .subject(request.getSubject())
                .body(request.getBody())
                .createdAt(LocalDateTime.now())
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
