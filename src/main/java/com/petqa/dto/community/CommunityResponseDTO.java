package com.petqa.dto.community;

import lombok.*;

public class CommunityResponseDTO {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PostListResponseDTO {
        private Long postId;
        private String title;
        private String image;
        private String relativeTime;
        private Long view;
        private Pet pet;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class Pet {
            private String name;
            private Double weight;
            private String species;
        }


    }
}
