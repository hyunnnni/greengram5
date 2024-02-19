package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_dm_msg")
public class DmMsgEntity extends CreatedAtEntity{

    @EmbeddedId//복합키 : 클래스id와 두가지 방법이 있다
    private DmMsgIds dmMsgIds;

    @ManyToOne
    @MapsId("idm")//이 멤버필드를 컬럼에 새롭게 추가하지 말고 dmMsgIds에 있는 컬럼과 연결해준다 만약 적지 않게 된다면
    //그냥 dmEntity인 idm이란 이름으로 컬럼이 더 추가되는 것이다
    @JoinColumn(name = "idm", columnDefinition = "BIGINT UNSIGNED")
    private DmEntity idm;

    @ManyToOne
    @JoinColumn(name = "iuser",nullable = false)
    private UserEntity iuser;

    @Column(length = 2000 , nullable = false)
    private String msg;

}
