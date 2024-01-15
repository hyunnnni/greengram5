package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.Const;
import com.greengram.greengram4.common.MyFileUtils;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.*;
import com.greengram.greengram4.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
    private final FeedMapper mapper;
    private final FeedCommentMapper comMapper;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils mfu;

    public ResVo postFeed(FeedInsDto dto){
/*        dto.setIuser(authenticationFacade.getLoginUserPk());
        log.info("dto : {}", dto);

        int result =  mapper.insFeed(dto);

        if(!dto.getPics().isEmpty()) {//앞의 배열이 비어있는지 아닌지 true, false 가려준다.
            FeedInsPicsDto pics = FeedInsPicsDto.builder()
                    .ifeed(dto.getIfeed())
                    .pics(dto.getPics())
                    .build();
            mapper.insFeedPics(pics);
        }

        return new ResVo(dto.getIfeed());*/

        dto.setIuser(authenticationFacade.getLoginUserPk());
        log.info("dto: {}", dto);
        int feedAffectedRows = mapper.insFeed(dto);
        log.info("feedAffectedRows: {}", feedAffectedRows);
        String target = "/feed/"+ dto.getIfeed();

        FeedPicsInsDto pdto = new FeedPicsInsDto();
        for(MultipartFile file : dto.getPics()){
            String saveFileNm = mfu.transferTo(file, target);
            pdto.getPics().add(saveFileNm);
        }
        pdto.setIfeed(dto.getIfeed());

        int feedPicsAffectsedRows = mapper.insFeedPics(pdto);
        return new ResVo(dto.getIfeed());
    }

    public List<FeedSelVo> getFeedAll(FeedSelDto dto){
        List<FeedSelVo> vo = mapper.feedSelAll(dto);

        FeedCommentSelDto fcDto = FeedCommentSelDto.builder()
                .startIdx(0)
                .rowCount(4)
                .build();

        for(FeedSelVo a : vo){
            a.setPics(mapper.feedSelPics(a.getIfeed()));

            fcDto.setIfeed(a.getIfeed());
            List<FeedCommentSelVo> comments = comMapper.selFeedCommentAll(fcDto);
            a.setComments(comments);

            if(comments.size() == 4){
                a.setIsMoreComment(Const.FEED_COMMENT_MORE);
                comments.remove(comments.size()-1);

            }
        }
        return vo;
    }

    public ResVo toggleFeedFav(FeedFavDto dto){
        int result = mapper.delFeedFav(dto);
        if(result == 1){
            return new ResVo(Const.FEED_FAV_DEL);
        }
        result = mapper.insFeedFav(dto);
        return new ResVo(Const.FEED_FAV_ADD);
    }

    public ResVo DelFeed(FeedDelDto dto) {

        int result = mapper.delComFavPics(dto);
        if(result == 1) {
            result = mapper.delFeed(dto);
            return new ResVo(result);
        }
        return new ResVo(result);
    }


}
