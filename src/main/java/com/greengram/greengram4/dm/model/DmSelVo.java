package com.greengram.greengram4.dm.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DmSelVo {
    private int idm;
    private String lastMsg;
    private String lastMsgAt;
    private String otherPersonNm;
    private String otherPersonPic;
    private int otherPersonIuser;
}
