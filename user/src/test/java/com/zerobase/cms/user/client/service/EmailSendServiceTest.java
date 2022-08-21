package com.zerobase.cms.user.client.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.config.FeignConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FeignConfig.class)
public class EmailSendServiceTest {

    @Autowired
    private MailgunClient mailgunClient;

    @Test
    void emailTest() {
        // 숙제
        mailgunClient.sendEmail(null);


    }


}
