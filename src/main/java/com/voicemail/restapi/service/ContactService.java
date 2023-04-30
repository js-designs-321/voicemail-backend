package com.voicemail.restapi.service;

import com.voicemail.restapi.exception.ResourceNotFoundException;
import com.voicemail.restapi.model.Contact;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.ContactRepository;
import com.voicemail.restapi.util.ContactRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    private final UserAuthenticationService userAuthenticationService;

    public List<Contact> getAllContactsForCurrentUser(){
        User user = userAuthenticationService.getCurrentUser();
        return contactRepository.findAllByUser(user);
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
    }

    public Contact saveContact(ContactRequest contactRequest){
        User user = userAuthenticationService.getCurrentUser();
        var contact = Contact
                .builder()
                .name(contactRequest.getName())
                .email(contactRequest.getEmail())
                .user(user)
                .build();
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, ContactRequest contactRequest) {
        User user = userAuthenticationService.getCurrentUser();
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        contact.setEmail(contactRequest.getEmail());
        contact.setName(contactRequest.getName());
        return contactRepository.save(contact);
    }

    public void deleteContactById(Long id){
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        contactRepository.delete(contact);
    }

}
