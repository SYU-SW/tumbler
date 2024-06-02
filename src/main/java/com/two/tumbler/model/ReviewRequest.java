package com.two.tumbler.model;

import lombok.Getter;

@Getter
public class ReviewRequest {
    private String userId;
    private String productId;
    private String content;
    private int rating;

}
