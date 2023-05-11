package com.voicemail.restapi.service;

import com.voicemail.restapi.exception.ResourceNotFoundException;
import com.voicemail.restapi.model.Attachment;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.AttachmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class AttachmentServiceTest {

    private AttachmentService attachmentService;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private UserAuthenticationService userAuthenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        attachmentService = new AttachmentService(attachmentRepository, userAuthenticationService);
    }

    @Test
    public void testUploadAttachment() throws IOException {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "test data".getBytes()
        );

        Attachment expectedAttachment = Attachment.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .data(file.getBytes())
                .emailId(user.getEmail())
                .build();

        when(userAuthenticationService.getCurrentUser()).thenReturn(user);
        when(attachmentRepository.save(expectedAttachment)).thenReturn(expectedAttachment);

        // Act
        Attachment actualAttachment = attachmentService.uploadAttachment(file);

        // Assert
        verify(userAuthenticationService, times(1)).getCurrentUser();
        verify(attachmentRepository, times(1)).save(expectedAttachment);
        Assertions.assertEquals(expectedAttachment, actualAttachment);
    }

    @Test
    public void testGetAttachmentById() {
        // Arrange
        Long id = 1L;
        Attachment expectedAttachment = new Attachment();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(expectedAttachment));

        // Act
        Attachment actualAttachment = attachmentService.getAttachmentById(id);

        // Assert
        verify(attachmentRepository, times(1)).findById(id);
        Assertions.assertEquals(expectedAttachment, actualAttachment);
    }

    @Test
    public void testGetAttachmentByIdNotFound() {
        // Arrange
        Long id = 1L;

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> attachmentService.getAttachmentById(id)
        );
    }

    @Test
    public void testDeleteAttachment() {
        // Arrange
        Long id = 1L;
        Attachment expectedAttachment = new Attachment();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(expectedAttachment));

        // Act
        attachmentService.deleteAttachment(id);

        // Assert
        verify(attachmentRepository, times(1)).findById(id);
        verify(attachmentRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteAttachmentNotFound() {
        // Arrange
        Long id = 1L;

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> attachmentService.deleteAttachment(id)
        );
    }
}
