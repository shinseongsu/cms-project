package com.zerobase.cms.user.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfiguration
class SignUpApplicationTest {

    @Autowired
    private SignUpApplication signUpApplication;

    @Test
    void customerVerifyTest() {

    }

}