package com.two.tumbler.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document("products")
public class Product {

    @Getter
    @Id
    private String id;

    private String name;
    private String brand;
    private String image;
    private String description;
    private String spec;
    private String color;
    private double price;
    private String quantity; // 재고 수량

    // 재고 수량을 제거하는 메서드
    public void removeStock(int count) {
        int currentQuantity = Integer.parseInt(this.quantity);
        if (currentQuantity >= count) {
            this.quantity = Integer.toString(currentQuantity - count);
        } else {
            throw new IllegalStateException("Not enough stock to remove");
        }
    }

    // 재고 수량을 추가하는 메서드
    public void addStock(int count) {
        int currentQuantity = Integer.parseInt(this.quantity);
        this.quantity = Integer.toString(currentQuantity + count);
    }

}
