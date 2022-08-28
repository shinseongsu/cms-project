package com.zerobase.cms.order.application;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.client.order.OrderListMainForm;
import com.zerobase.cms.order.client.user.ChangeBalanceForm;
import com.zerobase.cms.order.client.user.CustomerDto;
import com.zerobase.cms.order.domain.entity.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import com.zerobase.cms.order.service.ProductItemService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrdersApplication {

    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;

    // 결제를 위해 필요한 것
    // 1번 : 물건들이 전부 주문 가능한 상태인지 확인
    // 2번 : 가격 변돌이 있었는지에 대해 확인
    // 3번 : 고객의 돈이 충분한지
    // 4번 : 결제 & 상품의 재고 관리
    @Transactional
    public void order(String token, Cart cart) {
        Cart orderCart = cartApplication.refreshCart(cart);

        if(orderCart.getMessages().size() > 0) {
            // 문제가 있음
            throw new CustomException(ErrorCode.ORDER_FAIL_CART);
        }
        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

        int totalPrice = getTotalPrice(cart);
        if(customerDto.getBalance() < totalPrice) {
            throw new CustomException(ErrorCode.ORDER_FAIL_NO_MONEY);
        }

        userClient.changeBalance(token,
            ChangeBalanceForm.builder()
                .from("USER")
                .message("Order")
                .money(-totalPrice)
                .build());

        for(Cart.Product product : orderCart.getProducts()) {
            for(Cart.ProductItem cartItem : product.getItems()) {
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.setCount(productItem.getCount()-cartItem.getCount());
            }
        }

        // 주문 후, 상품 내역 보내기
        userClient.sendmailOfOrderList(token,
            OrderListMainForm.of(customerDto.getEmail(), cart.getProducts(), totalPrice));
    }

    public Integer getTotalPrice(Cart cart) {
        return cart.getProducts().stream()
            .flatMapToInt(product -> product.getItems().stream()
                .flatMapToInt(productItem -> IntStream.of(
                    productItem.getPrice()*productItem.getCount())))
            .sum();
    }



}
