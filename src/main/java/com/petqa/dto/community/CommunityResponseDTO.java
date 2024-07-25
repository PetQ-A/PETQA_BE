package com.petqa.dto.community;

import lombok.*;

import java.util.List;

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

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PostResponseDTO {
        private String title;
        private String content;
        private List<String> image;
        private String relativeTime;
        private Long view;
        private Long comment;
        private Pet pet;
        private User user;
        private Vote vote;


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

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class User {
            private String nickname;
            private String profile;
            private Long userId;
        }

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class Vote {
            private Boolean multi;
            private List<Item> item;

            @Getter
            @Setter
            @Builder
            @AllArgsConstructor
            @RequiredArgsConstructor
            public static class Item {
                private Long voteItemId;
                private String content;
            }
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class CommentResponseDTO {
        private Long commentId;
        private User user;
        private String content;
        private String relativeTime;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class User {
            private String nickname;
            private String profile;
            private Long userId;
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class ReplyResponseDTO {
        private Long replyId;
        private User user;
        private Tag tag;
        private String content;
        private String relativeTime;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class User {
            private String nickname;
            private String profile;
            private Long userId;
        }

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class Tag {
            private String nickname;
            private Long userId;
        }
    }
}
