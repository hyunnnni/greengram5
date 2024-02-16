package com.greengram.greengram4.feed.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FeedPicsInsDto {
    private Long ifeed;
    private List<String> pics = new ArrayList<>();
}
