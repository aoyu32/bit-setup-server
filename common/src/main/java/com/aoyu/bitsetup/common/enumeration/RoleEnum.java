package com.aoyu.bitsetup.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    USER("user"),
    VIP("vip"),
    ADMIN("admin");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}