package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.application.MailApplication;
import com.zerobase.cms.user.domain.mail.OrderListMailForm;
import com.zerobase.domain.config.JwtAuthenticationprovider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail/send")
public class MailController {
    private final MailApplication mailApplication;
    private final JwtAuthenticationprovider provider;

    @PostMapping("/orderList")
    public ResponseEntity<Void> sendmailOfOrderList(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                    @RequestBody OrderListMailForm orderListMailForm) {
        mailApplication.sendmailOfOrderList(provider.getUserVo(token).getId(), orderListMailForm);
        return ResponseEntity.ok().build();
    }
}
