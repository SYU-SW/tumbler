package com.two.tumbler.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    private String id;

    @DBRef
    private User user;

    private List<OrderProduct> orderProducts = new ArrayList<>();

    private String orderName;
    private double totalAmount;
    private String currency;
    private String payMethod;
    private LocalDateTime orderDate;

    @Field("status")
    private OrderStatus status;

    @Builder
    public Order(String id, User user, List<OrderProduct> orderProducts, String orderName, double totalAmount, String currency, String payMethod, LocalDateTime orderDate, OrderStatus status) {
        Assert.notNull(user, "User is required");
        Assert.notNull(orderProducts, "Order products are required");
        Assert.hasText(orderName, "Order name is required");
        Assert.hasText(currency, "Currency is required");
        Assert.hasText(payMethod, "Payment method is required");
        Assert.notNull(orderDate, "Order date is required");
        Assert.notNull(status, "Status is required");

        this.id = id;
        this.user = user;
        this.orderProducts = orderProducts;
        this.orderName = orderName;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.payMethod = payMethod;
        this.orderDate = orderDate;
        this.status = status;

        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrder(this);
        }
    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getOrders().contains(this)) {
            user.getOrders().add(this);
        }
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public static Order createOrder(User user, OrderProduct... orderProducts) {
        Order order = new Order();
        order.setUser(user);
        for (OrderProduct orderProduct : orderProducts) {
            order.addOrderProduct(orderProduct);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public void cancel() {
        this.setStatus(OrderStatus.CANCEL);
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.cancel();
        }
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getTotalPrice();
        }
        return totalPrice;
    }
}
