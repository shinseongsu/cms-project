package com.zerobase.cms.user.application;

import com.zerobase.cms.user.domain.SignInForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.CustomerService;
import com.zerobase.domain.common.UserType;
import com.zerobase.domain.config.JwtAuthenticationprovider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final JwtAuthenticationprovider provider;

    public String customerLoginToken(SignInForm form) {
        Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
            .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

        return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
    }
}
