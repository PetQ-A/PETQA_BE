package com.petqa.domain;

import com.petqa.domain.Mapping.UserQuestion;
import com.petqa.domain.common.MutableBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "\"user\"")
public class User extends MutableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Max(1000)
    @Setter
    private Integer points;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer questionCount;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Pet pet;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Diary> diary;



    public void incrementQuestionCount() {
        this.questionCount++;
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserQuestion> userQuestionsList = new ArrayList<>();


}
