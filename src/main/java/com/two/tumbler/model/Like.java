package com.two.tumbler.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "likes")
@Getter
@Setter
@NoArgsConstructor
public class Like {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    public Like(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}
