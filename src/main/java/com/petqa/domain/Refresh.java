package com.petqa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Refresh {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String socialId;
    private String refresh;
    private String expiration;
}
