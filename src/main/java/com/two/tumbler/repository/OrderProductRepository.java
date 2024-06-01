package com.two.tumbler.repository;

import com.two.tumbler.model.OrderProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderProductRepository extends MongoRepository<OrderProduct, String> {
}
