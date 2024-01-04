package com.greengram.greengram4.dm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmInsDto {//pk만 보통 받는 편이고 만약 성능 상 좋은 건 프론트 측에서 보낼 때
    //이름과 프사도 같이 주면 좋다
    @JsonIgnore
    private int idm;
    private int loginedIuser;
    private int otherPersonIuser;
}
