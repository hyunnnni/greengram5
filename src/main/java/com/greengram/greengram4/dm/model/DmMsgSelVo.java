package com.greengram.greengram4.dm.model;

import lombok.Data;

@Data
public class DmMsgSelVo {
    //idm값은 프론트에서 알고있기 때문에 여기서 안 보내줘도 된다
    private int seq;
    private int writerIuser;
    private String writerPic;
    private String msg;
    private String createdAt;
}
