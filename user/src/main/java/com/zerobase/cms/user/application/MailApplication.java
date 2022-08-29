package com.zerobase.cms.user.application;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.mail.OrderListMailForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.CustomerService;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailApplication {

    private final MailgunClient mailgunClient;
    private final CustomerService customerService;

    public void sendmailOfOrderList(Long id, OrderListMailForm orderListMailForm) {
        Customer customer = customerService.findByIdAndEmail(id, orderListMailForm.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        SendMailForm sendMailForm = SendMailForm.builder()
            .from("admin@gmail.com")
            .to(customer.getEmail())
            .subject("주문 내역입니다.")
            .text(orderListMailForm.getProductInfos().stream()
                .map(productInfo -> productInfo.makeText())
                .collect(Collectors.joining()))
            .build();

        mailgunClient.sendEmail(sendMailForm);
    }


}
