package com.greengram.greengram4.feed.model;

import lombok.Data;

@Data
public class FeedCommentSelVo {
    private int ifeedComment;
    private int writerIuser;
    private String comment;
    private String createdAt;
    private String writerNm;
    private String writerPic;
}
