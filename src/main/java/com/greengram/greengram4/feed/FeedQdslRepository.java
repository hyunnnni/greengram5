package com.greengram.greengram4.feed;

import com.greengram.greengram4.entity.UserEntity;
import com.greengram.greengram4.feed.model.FeedSelVo;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface FeedQdslRepository {
    List<FeedSelVo> selFeedAll(long loginIuser, long targetIuser, Pageable pageable);
}
