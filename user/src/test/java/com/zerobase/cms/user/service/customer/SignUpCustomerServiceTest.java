package com.zerobase.cms.user.service.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService signUpCustomerService;

    @Autowired
    private CustomerRepository customerRepository;

    Customer customer;

    @BeforeEach
    void setup() {
        customer = customerRepository.save(Customer.builder()
            .email("test@naver.com")
            .verify(true)
            .verificationCode("testCode")
            .verifyExpiredAt(LocalDateTime.now())
            .build());
    }

    @DisplayName("회원 가입 테스트 합니다.")
    @Test
    void signUpTest() {
        Customer result = signUpCustomerService.signUp(createSignUpForm("test1@gmail.com"));

        Customer findCustomer = customerRepository.findByEmail("test1@gmail.com")
                .orElseThrow(RuntimeException::new);

        assertThat(result).isEqualTo(findCustomer);
    }

    private SignUpForm createSignUpForm(String email) {
        return SignUpForm.builder()
            .email(email)
            .name("testName")
            .password("passsword")
            .birth(LocalDate.now())
            .phone("010-1234-5678")
            .build();
    }

    @DisplayName("이메일이 존재하면 true로 반환한다.")
    @Test
    void isEmailExistTest() {
        boolean result = signUpCustomerService.isEmailExist("test@naver.com");

        assertThat(result).isTrue();
    }

    @DisplayName("이메일이 존재하지 않으면 false로 반환")
    @Test
    void isEmailNotExistTest() {
        boolean result = signUpCustomerService.isEmailExist("test@gmail.com");

        assertThat(result).isFalse();
    }

    @DisplayName("이미 인증된 이메일일 경우 에러를 반환한다.")
    @Test
    void alreadyVerifyEmailTest() {
        assertThatExceptionOfType(CustomException.class).isThrownBy(() -> {
            signUpCustomerService.verifyEmail("test@naver.com", "testCode");
        }).withMessage("이미 인증이 완료되었습니다.");
    }

    @DisplayName("잘못된 인증번호를 받았을 시 에러를 반환합니다.")
    @Test
    void notEqualsVerificationCode() {
        customer.setVerify(false);
        customerRepository.save(customer);

        assertThatExceptionOfType(CustomException.class).isThrownBy(() -> {
            signUpCustomerService.verifyEmail("test@naver.com", "test");
        }).withMessage("잘못된 인증 시도입니다.");
    }

    @DisplayName("인증시간이 지났을 경우 에러를 반환합니다.")
    @Test
    void isBeforeVerifiy() {
        customer.setVerify(false);
        customerRepository.save(customer);

        assertThatExceptionOfType(CustomException.class).isThrownBy(() -> {
            signUpCustomerService.verifyEmail("test@naver.com", "testCode");
        }).withMessage("인증시간이 만료되었습니다.");
    }

    @DisplayName("인증된 상태로 변경한다.")
    @Test
    void verifyEmailTest() {
        customer.setVerify(false);
        customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
        customerRepository.save(customer);

        signUpCustomerService.verifyEmail("test@naver.com", "testCode");

        Customer result = customerRepository.findByEmail("test@naver.com")
            .orElseThrow(RuntimeException::new);

        assertThat(result.isVerify()).isTrue();
    }

    @Test
    void changeCustomerValidateEmailTest() {
        signUpCustomerService.changeCustomerValidateEmail(customer.getId(), "test");

        Customer result = customerRepository.findByEmail(customer.getEmail())
                .orElseThrow(RuntimeException::new);

        assertThat(result.getVerificationCode()).isEqualTo("test");
    }

}