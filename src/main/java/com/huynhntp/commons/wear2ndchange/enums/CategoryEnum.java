package com.huynhntp.commons.wear2ndchange.enums;

public enum CategoryEnum {
    TROUSERS,
    SHIRT,
    DRESS,
    JACKET;

    public static CategoryEnum parseStringToEnum(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Input string is null");
        }

        try {
            return CategoryEnum.valueOf(str.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + str);
        }
    }

}
