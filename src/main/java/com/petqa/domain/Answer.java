package com.petqa.domain;

import com.petqa.domain.common.BaseEntity;
import com.petqa.domain.mapping.UserQuestion;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<UserQuestion> userQuestionsList = new ArrayList<>();
}