package com.zerobase.cms.user.client.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendService {
    private final MailgunClient mailgunClient;

    public Response sendEmail() {

        SendMailForm form = SendMailForm.builder()
            .from("tlstjdtn321@naver.com")
            .to("tlstjdtn321@naver.com")
            .subject("test email from")
            .text("my test")
            .build();
        Response response = mailgunClient.sendEmail(form);

        return response;
    }
}
