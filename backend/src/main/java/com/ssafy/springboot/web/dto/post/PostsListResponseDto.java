package com.ssafy.springboot.web.dto.post;

import com.ssafy.springboot.domain.post.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long post_id;
    private String user_email;
    private Long board_id;
    private String title;
    private String content;
    private Long like_cnt;
    private Long comment_cnt;
    private LocalDateTime created_date;
    private LocalDateTime modified_date;

    public PostsListResponseDto(Posts entity) {
        this.post_id = entity.getPost_id();
        this.user_email = entity.getUser().getEmail();
        this.board_id = entity.getBoard().getBoard_id();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.like_cnt = entity.getLike_cnt();
        this.comment_cnt = entity.getComment_cnt();
        this.created_date = entity.getCreatedDate();
        this.modified_date = entity.getModifiedDate();
    }
}