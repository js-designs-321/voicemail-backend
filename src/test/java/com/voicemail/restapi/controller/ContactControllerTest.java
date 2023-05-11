package com.voicemail.restapi.controller;


import com.voicemail.restapi.model.Contact;
import com.voicemail.restapi.service.ContactService;
import com.voicemail.restapi.util.ContactRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @Test
    public void testGetAllContacts() {
        // Setup
        Contact contact1 = new Contact();
        Contact contact2 = new Contact();
        List<Contact> contacts = Arrays.asList(contact1, contact2);
        when(contactService.getAllContactsForCurrentUser()).thenReturn(contacts);

        // Test
        ResponseEntity<List<Contact>> responseEntity = contactController.getAllContacts();

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
        verify(contactService).getAllContactsForCurrentUser();
    }

    @Test
    public void testGetContactById() {
        // Setup
        Contact contact = new Contact();
        when(contactService.getContactById(1L)).thenReturn(contact);

        // Test
        ResponseEntity<Contact> responseEntity = contactController.getContactById(1L);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(contact, responseEntity.getBody());
        verify(contactService).getContactById(1L);
    }

    @Test
    public void testCreateContact() {
        // Setup
        ContactRequest contactRequest = new ContactRequest();
        Contact createdContact = new Contact();
        when(contactService.saveContact(contactRequest)).thenReturn(createdContact);

        // Test
        ResponseEntity<Contact> responseEntity = contactController.createContact(contactRequest);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(createdContact, responseEntity.getBody());
        verify(contactService).saveContact(contactRequest);
    }

    @Test
    public void testUpdateContact() {
        // Setup
        ContactRequest contactRequest = new ContactRequest();
        Contact updatedContact = new Contact();
        when(contactService.updateContact(1L, contactRequest)).thenReturn(updatedContact);

        // Test
        ResponseEntity<Contact> responseEntity = contactController.updateContact(1L, contactRequest);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedContact, responseEntity.getBody());
        verify(contactService).updateContact(1L, contactRequest);
    }

    @Test
    public void testDeleteContact() {
        // Test
        ResponseEntity<String> responseEntity = contactController.deleteContact(1L);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Contact successfully deleted", responseEntity.getBody());
        verify(contactService).deleteContactById(1L);
    }
}

