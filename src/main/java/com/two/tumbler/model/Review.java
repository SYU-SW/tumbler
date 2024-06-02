package com.two.tumbler.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document("reviews")
public class Review {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    private String content;
    private int rating;
    private LocalDateTime date;

    @Builder
    public Review(User user, Product product, String content, int rating, LocalDateTime date) {
        this.user = user;
        this.product = product;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }
}
