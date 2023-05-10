package com.voicemail.restapi.util;

import com.voicemail.restapi.model.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailInfo {

    private String to;
    private String subject;
    private String body;
    private Attachment attachment;

}
