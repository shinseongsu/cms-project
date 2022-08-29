package com.zerobase.cms.user.service.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    Customer customer;

    @BeforeEach
    void setup() {
        customer = customerRepository.save(createCustomer("test@naver.com"));
    }

    private Customer createCustomer(String email) {
        return Customer.builder()
            .email(email)
            .password("password")
            .verify(true)
            .build();
    }

    @DisplayName("이메일이 맞는지 확인하는 테스트 한다.")
    @Test
    void findByIdAndEmailTest() {
        Customer result = customerService.findByIdAndEmail(customer.getId(), "test@naver.com")
            .orElseThrow(RuntimeException::new);

        assertThat(customer).isEqualTo(result);
    }

    @DisplayName("이메일이 안맞아 null을 반환하는 테스트를 한다.")
    @Test
    void findByIdAndEmailException() {
        Optional<Customer> customerOptional = customerService.findByIdAndEmail(customer.getId(), "123@naver.com");

        assertThat(customerOptional.isEmpty()).isTrue();
    }

    @DisplayName("비밀번호와 인증되어 있는 회원만 리턴한다.")
    @Test
    void findValidCustomerTest() {
        Customer result = customerService.findValidCustomer("test@naver.com", "password")
            .orElseThrow(RuntimeException::new);

        assertThat(customer).isEqualTo(result);
    }

}