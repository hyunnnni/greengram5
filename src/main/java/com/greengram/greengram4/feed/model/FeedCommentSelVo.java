package com.greengram.greengram4.feed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedCommentSelVo {
    private int ifeedComment;
    private int writerIuser;
    private String comment;
    private String createdAt;
    private String writerNm;
    private String writerPic;
    private long ifeed;
}
