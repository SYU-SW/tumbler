package com.two.tumbler.repository;

import com.two.tumbler.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String userId);
}
