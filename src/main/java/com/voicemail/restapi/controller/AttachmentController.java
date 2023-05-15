package com.voicemail.restapi.controller;


import com.voicemail.restapi.model.Attachment;
import com.voicemail.restapi.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("voicemail/v1/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/")
    public ResponseEntity<Attachment> uploadAttachment(@RequestParam MultipartFile file){
        var attachment = attachmentService.uploadAttachment(file);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte []> downloadAttachment(@PathVariable Long id){
        Attachment attachment = attachmentService.getAttachmentById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(attachment.getFileType()));
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(attachment.getFileName()).build());
        return ResponseEntity.ok().headers(headers).body(attachment.getData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttachment(@PathVariable Long id){
        attachmentService.deleteAttachment(id);
        return ResponseEntity.ok("Attachment deleted successfully.");
    }

}
