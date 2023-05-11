package com.voicemail.restapi.service;

import com.voicemail.restapi.model.Draft;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.DraftRepository;
import com.voicemail.restapi.repository.UserRepository;
import com.voicemail.restapi.util.MailInfo;
import org.junit.Assert;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DraftServiceTest {

    @Mock
    private DraftRepository draftRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserAuthenticationService userAuthenticationServiceMock;

    @InjectMocks
    private DraftService draftService;

    @Test
    public void testGetAllDraftsForCurrentUser() {
        User currentUser = new User();
        currentUser.setEmail("user@example.com");

        Draft draft1 = new Draft();
        draft1.setId(1L);
        draft1.setFromEmail(currentUser.getEmail());

        Draft draft2 = new Draft();
        draft2.setId(2L);
        draft2.setFromEmail("other@example.com");

        List<Draft> expectedDrafts = Arrays.asList(draft1);

        Mockito.when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(draftRepositoryMock.findAllByFromEmail(currentUser.getEmail())).thenReturn(expectedDrafts);

        List<Draft> actualDrafts = draftService.getAllDraftsForCurrentUser();

        Assert.assertEquals(expectedDrafts, actualDrafts);
    }

    @Test
    public void testSaveDraft() {
        User currentUser = new User();
        currentUser.setEmail("user@example.com");

        User receiver = new User();
        receiver.setEmail("receiver@example.com");

        MailInfo draftInfo = new MailInfo();
        draftInfo.setTo(receiver.getEmail());
        draftInfo.setBody("Hello world");
        draftInfo.setSubject("Test email");
        draftInfo.setAttachment(null);

        Draft expectedDraft = new Draft();
        expectedDraft.setToEmail(receiver.getEmail());
        expectedDraft.setFromEmail(currentUser.getEmail());
        expectedDraft.setBody(draftInfo.getBody());
        expectedDraft.setSubject(draftInfo.getSubject());
        expectedDraft.setAttachment(draftInfo.getAttachment());
        expectedDraft.setCreatedAt(LocalDateTime.now());
        expectedDraft.setUpdatedAt(LocalDateTime.now());

        Mockito.when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(userRepositoryMock.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        Mockito.when(draftRepositoryMock.save(Mockito.any(Draft.class))).thenReturn(expectedDraft);

        Draft actualDraft = draftService.saveDraft(draftInfo);

        Assert.assertEquals(expectedDraft, actualDraft);
    }

    @Test
    public void testUpdateDraft() throws Exception {
        Long draftId = 1L;
        String currentUserEmail = "testuser1@example.com";
        String receiverEmail = "testuser2@example.com";
        User currentUser = new User();
        currentUser.setEmail(currentUserEmail);
        User receiver = new User();
        receiver.setEmail(receiverEmail);
        Draft existingDraft = new Draft();
        existingDraft.setId(draftId);
        existingDraft.setFromEmail(currentUserEmail);
        existingDraft.setToEmail(receiverEmail);
        existingDraft.setSubject("Test Subject");
        existingDraft.setBody("Test Body");

        MailInfo draftInfo = new MailInfo();
        draftInfo.setTo(receiverEmail);
        draftInfo.setSubject("Updated Subject");
        draftInfo.setBody("Updated Body");

        Mockito.when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(userRepositoryMock.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));
        Mockito.when(draftRepositoryMock.findById(draftId)).thenReturn(Optional.of(existingDraft));
        Mockito.when(draftRepositoryMock.save(existingDraft)).thenReturn(existingDraft);

        Draft updatedDraft = draftService.updateDraft(draftId, draftInfo);
        assertNotNull(updatedDraft);
        assertEquals(existingDraft.getId(), updatedDraft.getId());
        assertEquals(existingDraft.getFromEmail(), updatedDraft.getFromEmail());
        assertEquals(receiver.getEmail(), updatedDraft.getToEmail());
        assertEquals(draftInfo.getSubject(), updatedDraft.getSubject());
        assertEquals(draftInfo.getBody(), updatedDraft.getBody());
        assertNotNull(updatedDraft.getUpdatedAt());
        verify(draftRepositoryMock, times(1)).findById(draftId);
        verify(userAuthenticationServiceMock, times(1)).getCurrentUser();
        verify(userRepositoryMock, times(1)).findByEmail(receiverEmail);
        verify(draftRepositoryMock, times(1)).save(existingDraft);
    }


}
