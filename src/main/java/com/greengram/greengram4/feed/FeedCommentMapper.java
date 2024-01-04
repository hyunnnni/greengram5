package com.greengram.greengram4.feed;

import com.greengram.greengram4.feed.model.FeedCommentDelDto;
import com.greengram.greengram4.feed.model.FeedCommentInsDto;
import com.greengram.greengram4.feed.model.FeedCommentSelDto;
import com.greengram.greengram4.feed.model.FeedCommentSelVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedCommentMapper {
    int insFeedComment(FeedCommentInsDto dto);

    List<FeedCommentSelVo> selFeedCommentAll(FeedCommentSelDto dto);

    int delFeedComment(FeedCommentDelDto dto);
}
