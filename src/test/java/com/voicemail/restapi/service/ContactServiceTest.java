package com.voicemail.restapi.service;

import com.voicemail.restapi.exception.ResourceNotFoundException;
import com.voicemail.restapi.model.Contact;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.ContactRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepositoryMock;

    @Mock
    private UserAuthenticationService userAuthenticationServiceMock;

    @InjectMocks
    private ContactService contactService;

    @Test
    public void testGetAllContactsForCurrentUser() {
        User currentUser = new User();
        List<Contact> expectedContacts = Arrays.asList(new Contact(), new Contact());

        Mockito.when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(contactRepositoryMock.findAllByUser(currentUser)).thenReturn(expectedContacts);

        List<Contact> actualContacts = contactService.getAllContactsForCurrentUser();

        Assert.assertEquals(expectedContacts, actualContacts);
        Mockito.verify(userAuthenticationServiceMock, Mockito.times(1)).getCurrentUser();
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).findAllByUser(currentUser);
    }

    @Test
    public void testGetContactById() {
        Long contactId = 1L;
        Contact expectedContact = new Contact();

        Mockito.when(contactRepositoryMock.findById(contactId)).thenReturn(Optional.of(expectedContact));

        Contact actualContact = contactService.getContactById(contactId);

        Assert.assertEquals(expectedContact, actualContact);
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).findById(contactId);
    }

    @Test
    public void testGetContactById_NotFound() {
        Long contactId = 1L;

        Mockito.when(contactRepositoryMock.findById(contactId)).thenReturn(Optional.empty());

        try {
            contactService.getContactById(contactId);
            Assert.fail("Expected ResourceNotFoundException was not thrown");
        } catch (ResourceNotFoundException ex) {
            Mockito.verify(contactRepositoryMock, Mockito.times(1)).findById(contactId);
        }
    }

    @Test
    public void testDeleteContactById() {
        Long contactId = 1L;
        Contact existingContact = new Contact();
        existingContact.setId(contactId);

        Mockito.when(contactRepositoryMock.findById(contactId)).thenReturn(Optional.of(existingContact));
        contactService.deleteContactById(contactId);
        Mockito.verify(contactRepositoryMock).delete(existingContact);
    }

}

