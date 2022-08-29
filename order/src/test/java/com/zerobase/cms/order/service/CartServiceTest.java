package com.zerobase.cms.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order.client.RedisClient;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.product.AddProductCartForm.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    @BeforeEach
    void setup() throws JsonProcessingException {
        redisTemplate.delete("1");

        AddProductCartForm form = createAddProductCartForm(1L);

        cartService.addCart(1L, form);
    }

    private AddProductCartForm createAddProductCartForm(Long sellerId) {
        ProductItem productItem = ProductItem.builder()
            .id(1L)
            .name("testItem")
            .count(10)
            .price(1000)
            .build();

        return AddProductCartForm.builder()
            .id(1L)
            .sellerId(sellerId)
            .name("testCart")
            .description("test")
            .items(List.of(productItem))
            .build();
    }

    @DisplayName("카트에 값이 있을 경우 cart값을 반환한다.")
    @Test
    void getCartTest() {
        Cart cart = cartService.getCart(1L);

        assertThat(cart).isNotNull();
    }

    @DisplayName("카트에 값이 없을 경우 null을 반환한다.")
    @Test
    void getCartNullTest() {
        Cart cart = cartService.getCart(2L);

        assertThat(cart).isEqualTo(new Cart());
    }

    @DisplayName("카트에 내용을 수정합니다.")
    @Test
    void putCartTest() {
        Cart cart = cartService.getCart(1L);
        cart.setMessages(List.of("테스트 메시지를 수정합니다."));

        cartService.putCart(1L, cart);

        Cart result = cartService.getCart(1L);

        assertThat(result.getMessages()).containsExactly("테스트 메시지를 수정합니다.");
    }

}