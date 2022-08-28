package com.zerobase.cms.user.service.seller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.domain.repository.SellerRepository;
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
class SellerServiceTest {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerService sellerService;

    Seller seller;

    @BeforeEach
    void setup() {
          seller = sellerRepository.save(Seller.builder()
            .email("seller@naver.com")
            .name("판매자")
            .password("1111")
            .phone("010-1234-5678")
            .birth(LocalDate.now())
            .verifyExpiredAt(LocalDateTime.now())
            .verificationCode("testCode")
            .verify(true)
            .build());

    }

    @DisplayName("이메일 인증이 된 판매자 아이디를 반환한다.")
    @Test
    void findValidSellerTest() {
        Seller result = sellerService.findValidSeller("seller@naver.com", "1111")
            .orElseThrow(RuntimeException::new);

        assertThat(result).isEqualTo(seller);
    }

    @DisplayName("회원가입 합니다.")
    @Test
    void signUpTest() {
        Seller seller = sellerService.signUp(createSignUpForm("test@naver.com"));
        Seller result = sellerRepository.findByEmail("test@naver.com")
                .orElseThrow(RuntimeException::new);

        assertThat(seller).isEqualTo(result);
    }

    private SignUpForm createSignUpForm(String email) {
        return SignUpForm.builder()
            .email(email)
            .name("테스트")
            .password("password")
            .birth(LocalDate.now())
            .phone("010-1234-5678")
            .build();
    }

    @DisplayName("회원가입된 이메일인지 확인합니다.")
    @Test
    void isEmailExistTest() {
        boolean result = sellerService.isEmailExist("seller@naver.com");

        assertThat(result).isTrue();
    }


    @DisplayName("이메일인증시, 이미 인증되어 된거면 예외를 반환한다.")
    @Test
    void isAlreadyVerifyTest() {

        assertThatExceptionOfType(CustomException.class).isThrownBy(() -> {
            sellerService.verifyEmail("seller@naver.com", "testCode");
        });
    }

    @DisplayName("이메일 인증시, 확인 코드가 다르면 예외를 반환한다.")
    @Test
    void isNotEqualsVerificationCodeTest() {
        seller.setVerify(false);
        seller.setVerificationCode("1234");
        sellerRepository.save(seller);

        assertThatExceptionOfType(CustomException.class).isThrownBy(() -> {
            sellerService.verifyEmail("seller@naver.com", "testCode");
        });
    }

    @DisplayName("이메일인증시, 인증 시간이 지났을경우")
    @Test
    void isBeforeVerifyExpiredAtTest() {
        seller.setVerify(false);
        seller.setVerifyExpiredAt(LocalDateTime.now().minusDays(1));
        sellerRepository.save(seller);

        assertThatExceptionOfType(CustomException.class).isThrownBy(() -> {
            sellerService.verifyEmail("seller@naver.com", "testCode");
        });
    }

    @DisplayName("이메일 확인 변경한다.")
    @Test
    void changeCustomerValidateEmailTest() {
        sellerService.changeCustomerValidateEmail(seller.getId(), "123");

        String verificationCode = sellerRepository.findById(seller.getId())
                .orElseThrow(RuntimeException::new)
                .getVerificationCode();

        assertThat(verificationCode).isEqualTo("123");
    }

}