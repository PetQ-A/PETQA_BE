package com.petqa.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    ALL_CATEGORY("전체"),
    MEDICAL("의료"),
    NANUM("판매나눔"),
    LEISURE("여가"),
    OTHERS("기타");

    private final String name;

    public static Category fromName(String name) {
        for (Category category : Category.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return Category.ALL_CATEGORY;
    }

}
