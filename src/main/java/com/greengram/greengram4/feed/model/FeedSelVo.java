package com.greengram.greengram4.feed.model;

import lombok.Data;

import java.util.List;

@Data

public class FeedSelVo {
    private int ifeed;
    private int writerIuser;
    private String writerNm;
    private String writerPic;
    private String contents;
    private String location;
    private List<String> pics;
    private String createdAt;
    private int isFav;
    private List<FeedCommentSelVo> comments;
    private int isMoreComment; //0:댓글이 더 없음 1: 댓글이 더 있음
}
