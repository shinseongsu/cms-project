package com.zerobase.cms.user.service.customer;

import static com.zerobase.cms.user.exception.ErrorCode.NOT_ENOUGH_BALANCE;
import static com.zerobase.cms.user.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import com.zerobase.cms.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {
    private static final Integer ZERO = 0;

    private final CustomerBalanceHistoryRepository customerBlanceHistoryRepository;
    private final CustomerRepository customerRepository;

    @Transactional(noRollbackFor = {CustomException.class})
    public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form) throws CustomException {
        CustomerBalanceHistory customerBalanceHistory =
            customerBlanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
                .orElse(defaultCustomerBalanceHistory(customerId));

        isEnoughBalance(customerBalanceHistory, form);

        customerBalanceHistory = CustomerBalanceHistory.builder()
            .changeMoney(form.getMoney())
            .currentMoney(customerBalanceHistory.getCurrentMoney() + form.getMoney())
            .description(form.getMessage())
            .fromMessage(form.getFrom())
            .customer(customerBalanceHistory.getCustomer())
            .build();
        customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());

        return customerBlanceHistoryRepository.save(customerBalanceHistory);
    }

    private CustomerBalanceHistory defaultCustomerBalanceHistory(Long customerId) {
        return CustomerBalanceHistory.builder()
            .changeMoney(ZERO)
            .currentMoney(ZERO)
            .customer(findCustomerById(customerId))
            .build();
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    private void isEnoughBalance(CustomerBalanceHistory customerBalanceHistory, ChangeBalanceForm form) {
        if(customerBalanceHistory.getCurrentMoney() + form.getMoney() < ZERO) {
            throw new CustomException(NOT_ENOUGH_BALANCE);
        }
    }


}
