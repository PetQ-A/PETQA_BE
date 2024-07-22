package com.petqa.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Region {

    SEOUL("서울시"),
    BUSAN("부산시"),
    DAEGU("대구시"),
    INCHEON("인천시"),
    GWANGJU("광주시"),
    DAEJEON("대전시"),
    ULSAN("울산시"),
    SEJONG("세종시"),
    GYEONGGI("경기도"),
    GANGWON("강원도"),
    CHUNGCHEONGBUK("충청북도"),
    CHUNGCHEONGNAM("충청남도"),
    JEOLLABUK("전라북도"),
    JEOLLANAM("전라남도"),
    GYEONGSANGBUK("경상북도"),
    GYEONGSANGNAM("경상남도"),
    JEJU("제주도");

    private final String name;
}
