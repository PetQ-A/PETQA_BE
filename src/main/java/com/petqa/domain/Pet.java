package com.petqa.domain;

import com.petqa.domain.common.MutableBaseEntity;
import com.petqa.domain.enums.PetType;
import com.petqa.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;


@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Pet extends MutableBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pet_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String species;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType petType;

    private LocalDate birth;

    private String profileImage;

    private double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
