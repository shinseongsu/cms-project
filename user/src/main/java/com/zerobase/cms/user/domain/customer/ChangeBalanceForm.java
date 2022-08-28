package com.zerobase.cms.user.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeBalanceForm {
    private String from;
    private String message;
    private Integer money;
}
