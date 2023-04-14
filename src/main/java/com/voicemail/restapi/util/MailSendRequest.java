package com.voicemail.restapi.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailSendRequest {

    private String to;
    private String subject;
    private String body;

}
