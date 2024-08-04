package com.petqa.dto.community;

import lombok.Getter;

import java.util.List;

public class CommunityRequestDTO {

    @Getter
    public static class PostCreateRequestDTO {
        private String region;
        private String category;
        private String title;
        private String content;
        private Vote vote;

        @Getter
        public static class Vote {
            private String title;
            private List<String> items;
            private Boolean multi;
            private Integer end;
        }
    }
}
