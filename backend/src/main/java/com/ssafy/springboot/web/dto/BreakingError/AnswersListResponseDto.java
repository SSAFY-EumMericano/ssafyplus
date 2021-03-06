package com.ssafy.springboot.web.dto.BreakingError;

import com.ssafy.springboot.domain.BreakingError.Answers;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswersListResponseDto {
    private Long answer_id;
    private String user_email;
    private Long error_id;
    private String title;
    private String content;
    private Long like_cnt;
    private Long answer_cnt;
    private LocalDateTime created_date;
    private LocalDateTime modified_date;

    public AnswersListResponseDto(Answers entity) {
        this.answer_id = entity.getAnswer_id();
        this.user_email = entity.getUser().getEmail();
        this.error_id = entity.getError().getError_id();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.like_cnt = entity.getLike_cnt();
        this.answer_cnt = entity.getAnswer_cnt();
        this.created_date = entity.getCreatedDate();
        this.modified_date = entity.getModifiedDate();
    }
}