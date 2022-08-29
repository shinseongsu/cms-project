package com.zerobase.cms.user.domain.mail;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderListMailForm {
    private String email;
    private List<ProductInfo> productInfos;
    private Integer totalBalance;

    @Getter @Setter
    @NoArgsConstructor
    public static class ProductInfo {
        private String name;
        private Integer count;
        private Integer price;

        public String makeText() {
            StringBuilder sb = new StringBuilder();
            sb.append("상품명: " + name);
            sb.append("갯수: " + count);
            sb.append("가격: " + price);
            return sb.toString();
        }
    }

}
