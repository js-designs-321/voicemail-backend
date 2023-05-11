package com.voicemail.restapi.controller;

import com.voicemail.restapi.model.Attachment;
import com.voicemail.restapi.service.AttachmentService;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AttachmentControllerTest {

    @Mock
    private AttachmentService attachmentService;

    @InjectMocks
    private AttachmentController attachmentController;

    @Test
    public void testUploadAttachment() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello, world!".getBytes()
        );
        Attachment mockAttachment = new Attachment();
        mockAttachment.setId(1L);
        mockAttachment.setFileName("test.txt");
        mockAttachment.setFileType("text/plain");
        mockAttachment.setData("Hello, world!".getBytes());

        when(attachmentService.uploadAttachment(mockFile)).thenReturn(mockAttachment);

        ResponseEntity<Attachment> response = attachmentController.uploadAttachment(mockFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAttachment, response.getBody());
    }

    @Test
    public void testDownloadAttachment() throws Exception {
        Attachment mockAttachment = new Attachment();
        mockAttachment.setId(1L);
        mockAttachment.setFileName("test.txt");
        mockAttachment.setFileType("text/plain");
        mockAttachment.setData("Hello, world!".getBytes());

        when(attachmentService.getAttachmentById(1L)).thenReturn(mockAttachment);

        ResponseEntity<byte[]> response = attachmentController.downloadAttachment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAttachment.getData(), response.getBody());
    }

    @Test
    public void testDeleteAttachment() throws Exception {
        ResponseEntity<String> response = attachmentController.deleteAttachment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Attachment deleted successfully.", response.getBody());

        verify(attachmentService).deleteAttachment(1L);
    }

}
