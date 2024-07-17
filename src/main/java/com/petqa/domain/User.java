package com.petqa.domain;


import com.petqa.domain.common.MutableBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User extends MutableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

//    @Enumerated(EnumType.STRING)
//    private SocialType socialType;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer points;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer questionCount;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Pet pet;


}
