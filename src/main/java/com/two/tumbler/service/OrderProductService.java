package com.two.tumbler.service;

import com.two.tumbler.model.OrderProduct;
import com.two.tumbler.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public OrderProduct createOrderProduct(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    public Optional<OrderProduct> getOrderProductById(String id) {
        return orderProductRepository.findById(id);
    }

    public void deleteOrderProduct(String id) {
        orderProductRepository.deleteById(id);
    }

    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }
}
