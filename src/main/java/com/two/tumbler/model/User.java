package com.two.tumbler.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
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
}
