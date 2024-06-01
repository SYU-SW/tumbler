package com.two.tumbler.service;

import com.two.tumbler.model.Order;
import com.two.tumbler.model.OrderProduct;
import com.two.tumbler.model.Product;
import com.two.tumbler.model.User;
import com.two.tumbler.repository.OrderRepository;
import com.two.tumbler.repository.ProductRepository;
import com.two.tumbler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderProductService orderProductService;

    public Order createOrder(Order order) {
        if (order.getUser() == null || order.getUser().getId() == null) {
            throw new IllegalArgumentException("User and User ID must not be null");
        }

        // 사용자 가져오기
        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + order.getUser().getId()));

        // 사용자 설정
        order.setUser(user);

        // 각 OrderProduct에 대해 처리
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            if (orderProduct.getProduct() == null || orderProduct.getProduct().getId() == null) {
                throw new IllegalArgumentException("Product and Product ID must not be null");
            }

            // 제품 가져오기
            Product product = productRepository.findById(orderProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderProduct.getProduct().getId()));

            // 재고 확인 및 업데이트
            if (Integer.parseInt(product.getQuantity()) < orderProduct.getCount()) {
                throw new RuntimeException("Not enough stock for product id: " + orderProduct.getProduct().getId());
            }
            product.removeStock(orderProduct.getCount());
            productRepository.save(product);
            orderProductService.createOrderProduct(orderProduct);
        }

        // 주문 저장
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(String id, Order orderDetails) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setUser(orderDetails.getUser());
                    order.setOrderProducts(orderDetails.getOrderProducts());
                    order.setOrderName(orderDetails.getOrderName());
                    order.setTotalAmount(orderDetails.getTotalAmount());
                    order.setCurrency(orderDetails.getCurrency());
                    order.setPayMethod(orderDetails.getPayMethod());
                    order.setOrderDate(orderDetails.getOrderDate());
                    order.setStatus(orderDetails.getStatus());
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            product.addStock(orderProduct.getCount());
            productRepository.save(product);
            orderProductService.deleteOrderProduct(orderProduct.getId());
        }

        orderRepository.deleteById(id);
    }

    public void cancelOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.cancel();
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            product.addStock(orderProduct.getCount());
            productRepository.save(product);
        }
        orderRepository.save(order);
    }
}
