package com.zerobase.cms.user.service.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import com.zerobase.cms.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerBalanceServiceTest {

    @Autowired
    private CustomerBalanceService customerBalanceService;

    @Autowired
    private CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    Customer customer;
    CustomerBalanceHistory customerBalanceHistory;

    @BeforeEach
    void setup() {
        customer = customerRepository.save(createCustomer("test@naver.com"));
        customerBalanceHistory = customerBalanceHistoryRepository.save(createCustomerBalanceHistory(customer));
    }

    private Customer createCustomer(String email) {
        return Customer.builder()
            .email(email)
            .build();
    }

    private CustomerBalanceHistory createCustomerBalanceHistory(Customer customer) {
        return CustomerBalanceHistory.builder()
            .customer(customer)
            .changeMoney(10000)
            .currentMoney(10000)
            .fromMessage("test")
            .description("test")
            .build();
    }

    @DisplayName("고객의 잔고를 변경한다.")
    @Test
    void changeBalanceTest() {
        customerBalanceService.changeBalance(customer.getId(), createChangeBalanceForm(-100));

        CustomerBalanceHistory customerBalanceHistory = customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customer.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(customerBalanceHistory.getCurrentMoney()).isEqualTo(9900);
    }

    @DisplayName("금액이 부족하여 에러를 반환합니다.")
    @Test
    void notEngoughBalanceException() {

        assertThatExceptionOfType(CustomException.class).isThrownBy(() -> {
            customerBalanceService.changeBalance(customer.getId(), createChangeBalanceForm(-100000));
        });
    }

    private ChangeBalanceForm createChangeBalanceForm(Integer money) {
        return new ChangeBalanceForm(customer.getEmail(), "test", money);
    }


}