package com.voicemail.restapi.service;

import com.voicemail.restapi.exception.ResourceNotFoundException;
import com.voicemail.restapi.model.Attachment;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final UserAuthenticationService userAuthenticationService;

    public Attachment uploadAttachment(MultipartFile file){
        User user = userAuthenticationService.getCurrentUser();
        Attachment attachment = null;
        try {
            attachment = Attachment
                    .builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .emailId(user.getEmail())           // so that user can list all attachment, he had sent
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload attachment",e);
        }
        return attachmentRepository.save(attachment);
    }

    public Attachment getAttachmentById(Long id){
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found","id",id));
    }

    public void deleteAttachment(Long id) {
        var attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found","id",id));
        attachmentRepository.deleteById(id);
    }


}
