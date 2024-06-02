package com.two.tumbler.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemRequest {

    private String userId;
    private String productId;
    private int quantity;
}
