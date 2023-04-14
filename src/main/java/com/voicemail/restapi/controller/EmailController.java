package com.voicemail.restapi.controller;

import com.voicemail.restapi.model.Email;
import com.voicemail.restapi.service.EmailService;
import com.voicemail.restapi.util.MailSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voicemail/v1/mail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/inbox")
    public ResponseEntity<List<Email>> getAllReceivedEmails(){
        List<Email> emails = emailService.getAllReceivedEmailsForCurrentUser();
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/send-box")
    public ResponseEntity<List<Email>> getAllSentEmails(){
        List<Email> emails = emailService.getAllSendEmailsForCurrentUser();
        return ResponseEntity.ok(emails);
    }

    @PostMapping("/send")
    public ResponseEntity<Email> sendEmail(@RequestBody MailSendRequest request){
        Email sendEmail = emailService.sendEmail(request);
        return ResponseEntity.ok(sendEmail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id){
        Email email = emailService.getEmailById(id);
        return ResponseEntity.ok(email);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmailById(@PathVariable Long id){
        emailService.deleteEmailById(id);
        return ResponseEntity.ok("Email Successfully Deleted");
    }


}
