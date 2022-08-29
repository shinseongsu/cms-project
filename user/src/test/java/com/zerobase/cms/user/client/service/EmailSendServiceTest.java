package com.zerobase.cms.user.client.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.config.FeignConfig;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfiguration
public class EmailSendServiceTest {

   @Autowired
   private EmailSendService emailSendService;

    @Test
    void emailTest() {
        Response response = emailSendService.sendEmail();

        assertThat(response.status()).isEqualTo(200);
    }


}
