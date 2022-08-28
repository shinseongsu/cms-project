package com.zerobase.cms.order.client.order;

import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.domain.redis.Cart.Product;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderListMainForm {

    private String email;
    private List<ProductInfo> productInfos;
    private Integer totalBalance;

    @Getter
    @AllArgsConstructor
    public static class ProductInfo {
        private String name;
        private Integer count;
        private Integer price;

        public static ProductInfo of(Cart.ProductItem productItem) {
            return new ProductInfo(productItem.getName(), productItem.getCount(), productItem.getPrice());
        }
    }

    public static OrderListMainForm of(String email, List<Product> products, int totalPrice) {
        List<ProductInfo> productInfos = products.stream()
            .flatMap(product -> product.getItems().stream()
                .map(ProductInfo::of))
            .collect(Collectors.toList());

        return new OrderListMainForm(email, productInfos, totalPrice);
    }

}
