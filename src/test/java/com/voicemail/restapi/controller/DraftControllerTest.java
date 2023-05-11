package com.voicemail.restapi.controller;

import com.voicemail.restapi.model.Draft;
import com.voicemail.restapi.service.DraftService;
import com.voicemail.restapi.util.MailInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DraftControllerTest {

    @Mock
    private DraftService draftService;

    @InjectMocks
    private DraftController draftController;

    @Test
    public void testGetAllDrafts() {
        List<Draft> drafts = Arrays.asList(
                new Draft(1L, "Draft 1", "Hello world!","sender@voicemail.com","reciever@voicemail.com", LocalDateTime.now(),LocalDateTime.now(),null),
                new Draft(2L, "Draft 2", "Bye world!","sender@voicemail.com","reciever@voicemail.com", LocalDateTime.now(),LocalDateTime.now(),null)
        );
        Mockito.when(draftService.getAllDraftsForCurrentUser()).thenReturn(drafts);
        ResponseEntity<List<Draft>> responseEntity = draftController.getAllDrafts();
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(drafts, responseEntity.getBody());
    }

    @Test
    public void testGetDraftById() {
        Draft draft = new Draft(1L, "Draft 1", "Hello world!","sender@voicemail.com","reciever@voicemail.com", LocalDateTime.now(),LocalDateTime.now(),null);
        Mockito.when(draftService.getDraftById(1L)).thenReturn(draft);
        ResponseEntity<Draft> responseEntity = draftController.getDraftById(1L);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(draft, responseEntity.getBody());
    }

    @Test
    public void testCreateDraft() {
        MailInfo mailInfo = new MailInfo("Draft 1", "Hello world!", null, null);
        Draft draft = new Draft(1L, "Draft 1", "Hello world!","sender@voicemail.com","reciever@voicemail.com", LocalDateTime.now(),LocalDateTime.now(),null);
        Mockito.when(draftService.saveDraft(mailInfo)).thenReturn(draft);
        ResponseEntity<Draft> responseEntity = draftController.createDraft(mailInfo);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(draft, responseEntity.getBody());
    }

    @Test
    public void testUpdateDraft() throws AccessDeniedException {
        MailInfo mailInfo = new MailInfo("Draft 1 updated", "Hello world! updated", null, null);
        Draft draft = new Draft(1L, "Draft 1", "Hello world! updated","sender@voicemail.com","reciever@voicemail.com", LocalDateTime.now(),LocalDateTime.now(),null);
        Mockito.when(draftService.updateDraft(1L, mailInfo)).thenReturn(draft);
        ResponseEntity<Draft> responseEntity = draftController.updateDraft(1L, mailInfo);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(draft, responseEntity.getBody());
    }

    @Test
    public void testDeleteDraft() {
        ResponseEntity<String> responseEntity = draftController.deleteDraft(1L);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals("Draft successfully deleted", responseEntity.getBody());
        Mockito.verify(draftService, Mockito.times(1)).deleteDraftById(1L);
    }

}
