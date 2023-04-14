package com.voicemail.restapi.repository;

import com.voicemail.restapi.model.Email;
import com.voicemail.restapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findAllByFromEmail(String fromEmail);

    List<Email> findAllByToEmail(String toEmail);

}
