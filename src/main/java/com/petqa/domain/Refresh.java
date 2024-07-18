package com.petqa.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;
}
