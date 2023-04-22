package com.example.library.utils;

public class AssertUtils {

    public static String errorMsg(String field) {
        return String.format("Поле %s не соответствует ожидаемому результату.", field);
    }
}
