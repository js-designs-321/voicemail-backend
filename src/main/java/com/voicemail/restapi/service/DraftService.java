package com.voicemail.restapi.service;

import com.voicemail.restapi.exception.ResourceNotFoundException;
import com.voicemail.restapi.model.Draft;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.DraftRepository;
import com.voicemail.restapi.repository.UserRepository;
import com.voicemail.restapi.util.MailInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DraftService {

    private final DraftRepository draftRepository;

    private final UserRepository userRepository;

    private final UserAuthenticationService userAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftService.class);

    public List<Draft> getAllDraftsForCurrentUser(){
        User currentUser = userAuthenticationService.getCurrentUser();
        return draftRepository.findAllByFromEmail(currentUser.getEmail());
    }

    public Draft saveDraft(MailInfo draftInfo){
        User draftCreatedBy = userAuthenticationService.getCurrentUser();
        User receiver = userRepository.findByEmail(draftInfo.getTo())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email address"));
        var draft = Draft.builder()
                .toEmail(receiver.getEmail())
                .fromEmail(draftCreatedBy.getEmail())
                .body(draftInfo.getBody())
                .subject(draftInfo.getSubject())
                .attachment(draftInfo.getAttachment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return draftRepository.save(draft);
    }

    public Draft updateDraft(Long id, MailInfo draftInfo) throws AccessDeniedException {
        User currentUser = userAuthenticationService.getCurrentUser();
        Draft draft = draftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Draft","id",id));
        if(!draft.getFromEmail().equals(currentUser.getEmail())){
            throw new AccessDeniedException("You are not authorized to update this draft");
        }
        User receiver = userRepository.findByEmail(draftInfo.getTo())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email address"));
        draft.setBody(draftInfo.getBody());
        draft.setToEmail(receiver.getEmail());
        draft.setSubject(draftInfo.getSubject());
        draft.setAttachment(draftInfo.getAttachment());
        draft.setUpdatedAt(LocalDateTime.now());
        return draftRepository.save(draft);
    }

    public Draft getDraftById(Long id){
        return draftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Draft","id",id));
    }

    public void deleteDraftById(Long id){
        if(!draftRepository.existsById(id)){
            throw new ResourceNotFoundException("Draft", "id", id);
        }
        draftRepository.deleteById(id);
    }

}
