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


    @Getter
    public static class CommentCreateRequestDTO {
        private String content;
    }

    @Getter
    public static class ReplyCreateRequestDTO {
        private String content;
        private Tag tag;

        @Getter
        public static class Tag {
            private String nickname;
            private Long userId;
        }
    }


    @Getter
    public static class VoteRequestDTO {

        private List<Item> item;

        @Getter
        public static class Item {
            private Long voteItemId;
        }
    }

}
