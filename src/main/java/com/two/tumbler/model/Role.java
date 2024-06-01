package com.two.tumbler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_GUEST", "사용자"),
    ADMIN("ROLE_USER", "관리자");

    private final String key;
    private final String title;
}
