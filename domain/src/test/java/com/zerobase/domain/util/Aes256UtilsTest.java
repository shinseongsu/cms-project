package com.zerobase.domain.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Aes256UtilsTest {

    @Test
    void encrypt() {
        String encrypt = Aes256Utils.encrypt("Hello world");
        assertEquals(Aes256Utils.decrypt(encrypt), "Hello world");
    }


}