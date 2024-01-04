package com.greengram.greengram4.dm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmMsgInsDto {
    private int idm;
    @JsonIgnore
    private int seq;
    private int loginedIuser;
    private String loginedPic;
    private String msg;
}
