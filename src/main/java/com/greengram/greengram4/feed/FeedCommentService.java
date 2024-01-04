package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.FeedCommentDelDto;
import com.greengram.greengram4.feed.model.FeedCommentInsDto;
import com.greengram.greengram4.feed.model.FeedCommentSelDto;
import com.greengram.greengram4.feed.model.FeedCommentSelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedCommentService {
    private final FeedCommentMapper mapper;
    public ResVo postFeedComment(FeedCommentInsDto dto) {
        int affectedRows = mapper.insFeedComment(dto);

        return new ResVo(dto.getIfeedComment());
    }

    public List<FeedCommentSelVo> getFeedCommentAll(int ifeed){
        FeedCommentSelDto dto = FeedCommentSelDto.builder()
                .ifeed(ifeed)
                .startIdx(3)
                .rowCount(999)
                .build();

        return mapper.selFeedCommentAll(dto);
    }

    public ResVo delComment(FeedCommentDelDto dto){
        int result = mapper.delFeedComment(dto);

        return new ResVo(result);
    }
}
