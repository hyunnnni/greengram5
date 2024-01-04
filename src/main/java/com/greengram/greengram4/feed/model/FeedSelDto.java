package com.greengram.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greengram.greengram4.common.Const;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class FeedSelDto {
    @Schema(title = "페이지")
    private int page;
    @Schema(title = "로그인한 유저pk")
    private int loginedIuser;

    @Schema(title = "원하는 유저pk")
    private int targetIuser;


    @Schema(title = "좋아요 Feed 리스트 여부")
    private int isFavList;

    @JsonIgnore
    private int startIdx;
    @JsonIgnore
    private int rowCount = Const.FEED_COUNT_PER_PAGE;

    public void setPage(int page){
        this.startIdx =(page-1)*rowCount;
    }

}
