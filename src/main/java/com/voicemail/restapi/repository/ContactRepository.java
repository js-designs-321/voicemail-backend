package com.voicemail.restapi.repository;

import com.voicemail.restapi.model.Contact;
import com.voicemail.restapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByUser(User user);

}
