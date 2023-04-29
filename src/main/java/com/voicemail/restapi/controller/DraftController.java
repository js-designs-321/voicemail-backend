package com.voicemail.restapi.controller;

import com.voicemail.restapi.model.Draft;
import com.voicemail.restapi.service.DraftService;
import com.voicemail.restapi.util.MailInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("voicemail/v1/draft")
@RequiredArgsConstructor
public class DraftController {

    private final DraftService draftService;

    @GetMapping("/")
    public ResponseEntity<List<Draft>> getAllDrafts(){
        List<Draft> drafts = draftService.getAllDraftsForCurrentUser();
        return ResponseEntity.ok(drafts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Draft> getDraftById(@PathVariable Long id){
        Draft draft = draftService.getDraftById(id);
        return ResponseEntity.ok(draft);
    }

    @PostMapping("/")
    public ResponseEntity<Draft> createDraft(@RequestBody MailInfo draftInfo){
        Draft createdDraft = draftService.saveDraft(draftInfo);
        return ResponseEntity.ok(createdDraft);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Draft> updateDraft(@PathVariable Long id, @RequestBody MailInfo draftinfo) throws AccessDeniedException {
        Draft updatedDraft = draftService.updateDraft(id, draftinfo);
        return ResponseEntity.ok(updatedDraft);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDraft(@PathVariable Long id) {
        draftService.deleteDraftById(id);
        return ResponseEntity.ok("Draft successfully deleted");
    }

}
