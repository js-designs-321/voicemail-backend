package com.voicemail.restapi.controller;

import com.voicemail.restapi.model.Contact;
import com.voicemail.restapi.service.ContactService;
import com.voicemail.restapi.util.ContactRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("voicemail/v1/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/")
    public ResponseEntity<List<Contact>> getAllContacts(){
        List<Contact> contacts = contactService.getAllContactsForCurrentUser();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id){
        Contact contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    @PostMapping("/")
    public ResponseEntity<Contact> createContact(@RequestBody ContactRequest contactRequest){
        Contact createdContact = contactService.saveContact(contactRequest);
        return ResponseEntity.ok(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody ContactRequest contactRequest){
        Contact updatedContact = contactService.updateContact(id, contactRequest);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        contactService.deleteContactById(id);
        return ResponseEntity.ok("Contact successfully deleted");
    }


}
