package com.petqa.domain;

import com.petqa.domain.common.BaseEntity;
import com.petqa.domain.common.MutableBaseEntity;
import com.petqa.dto.diary.DiaryResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.joda.time.DateTime;
import java.time.LocalDate;

import java.util.Base64;
import java.util.Date;

@Getter
@Entity
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary extends MutableBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    private String img;

    @Column(length = 18)
    private String memo;

    private LocalDate date; // 이름

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

//    public DiaryResponseDTO toResponseDTO() {
//        return DiaryResponseDTO.builder()
//                .memo(memo)
//                .date(date)
//                .img(img)
//                .build();
//    }
}

