package com.two.tumbler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    @Getter
    private String id;

    private String name;
    private String profileImage;
    private String email;
    private String phoneNumber;
    private Address address;
    private String provider;
    private String role;

    @Getter
    @Setter
    public static class Address {
        private String zipcode;
        private String addr;
        private String addrDetail;
    }

    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
}
