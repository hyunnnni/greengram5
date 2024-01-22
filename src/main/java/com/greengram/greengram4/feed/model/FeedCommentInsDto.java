package com.greengram.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class FeedCommentInsDto {
    @JsonIgnore
    private int ifeedComment;
    @Range(min = 1)//max도 넣을 수 있음
    private int ifeed;
    @Min(1)//min값만 넣을 때 사용
    private int iuser;
    @NotEmpty(message = "댓글 내용을 입력해주세요")//null또는 비어있으면 안됨
    @Size(min = 1)//사이즈
    private String comment;

    //해당 애노테이션에 걸릴 시 MethodArgument 어쩌구가 뜬다
}
