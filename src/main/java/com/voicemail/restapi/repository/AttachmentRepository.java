package com.voicemail.restapi.repository;

import com.voicemail.restapi.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}