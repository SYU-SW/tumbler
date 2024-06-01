package com.two.tumbler.repository;

import com.two.tumbler.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUser_Name(String name);
}
