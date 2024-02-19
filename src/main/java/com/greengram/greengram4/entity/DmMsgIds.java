package com.greengram.greengram4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Embeddable
@EqualsAndHashCode//같은 레코드 select시 true가 되게 함
public class DmMsgIds implements Serializable {

    private Long idm;
    @Column(columnDefinition = "BIGINT UNSIGNED")
    //외래키를 따로 걸 필요가 없다면 이 클래스에만 적어도 된다
    private Long seq;
}
