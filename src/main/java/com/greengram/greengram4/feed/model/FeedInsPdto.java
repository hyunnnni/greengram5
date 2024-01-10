package com.greengram.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedInsPdto {
    private int ifeed;
    private int iuser;
    private String contents;
    private String location;
}
