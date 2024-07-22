package com.petqa.domain.enums;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {
    MEDICAL("의료"),
    NANUM("판매나눔"),
    LEISURE("여가"),
    OTHERS("기타");

    private final String name;
}
