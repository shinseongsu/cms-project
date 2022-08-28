package com.zerobase.cms.order.domain.repository;

import com.zerobase.cms.order.domain.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchByName(String name);
}
