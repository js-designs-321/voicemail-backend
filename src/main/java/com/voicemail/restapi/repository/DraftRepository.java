package com.voicemail.restapi.repository;

import com.voicemail.restapi.model.Draft;
import com.voicemail.restapi.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftRepository extends JpaRepository<Draft, Long> {

    List<Draft> findAllByFromEmail(String fromEmail);

}
