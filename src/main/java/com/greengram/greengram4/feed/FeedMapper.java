package com.greengram.greengram4.feed;

import com.greengram.greengram4.feed.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed (FeedInsDto dto);

    int insFeedPics(FeedInsDto dto);

    List<FeedSelVo> feedSelAll(FeedSelDto dto);

    List<String> feedSelPics (int ifeed);

    int delFeedFav (FeedFavDto dto);

    int insFeedFav(FeedFavDto dto);

    int selFeedTest(int ifeed);

    int delComFavPics(FeedDelDto dto);

    int delFeed(FeedDelDto dto);


    List<FeedFavDto> selFeedFavForTest(FeedFavDto dto);

    int delFeedFavAll(int ifeed);
}
