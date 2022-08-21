package com.zerobase.cms.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService service;

    @Test
    void setup() {
        SignUpForm form = SignUpForm.builder()
            .name("name")
            .birth(LocalDate.now())
            .email("tlstjdtn321@naver.com")
            .password("1")
            .phone("01012345678")
            .build();

        Customer c = service.signUp(form);

        assertNotNull(c.getId());
        assertNotNull((c.getCreatedAt()));
    }

}