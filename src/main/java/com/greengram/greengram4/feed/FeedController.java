package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
@Tag(name = "피드 API", description = "피드 관련 처리")
public class FeedController {
    private final FeedService service;


    @PostMapping
    @Operation(summary = "피드 등록" , description = "피드 등록 처리")
    public ResVo PostFeed(@RequestBody FeedInsDto dto){
        log.info("dto : {}", dto);
        ResVo vo = service.postFeed(dto);
        return vo;

    }


    @GetMapping
    @Operation(summary = "피드 리스트" ,description = "전체 피드 리스트, 특정 사용자")
    public List<FeedSelVo> getFeedAll(FeedSelDto dto){
        return service.getFeedAll(dto);
        //service.getFeedAll(dto);
        //return new ArrayList<>();
    }

    @GetMapping("/fav")
    @Operation(summary = "좋아요 처리", description = "좋아요 누르기/ 취소")
    @Parameters(value = {
            @Parameter(name = "ifeed", description = "좋아요 피드"),
            @Parameter(name = "iuser", description = "좋아요를 하는/취소하는 유저")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "e")
    })
    public ResVo toggleFeedFav(FeedFavDto dto){
        return service.toggleFeedFav(dto);
    }

    @DeleteMapping
    public ResVo delFeed(FeedDelDto dto){
        log.info("DelDto : {}", dto);
        return service.DelFeed(dto);
    }


}
