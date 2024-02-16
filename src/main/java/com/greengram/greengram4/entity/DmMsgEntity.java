package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_dm_msg")
public class DmMsgEntity extends CreatedAtEntity{

    @EmbeddedId
    private DmMsgIds dmMsgIds;

    @ManyToOne
    @MapsId("idm")
    @JoinColumn(name = "idm", columnDefinition = "BIGINT UNSIGNED")
    private DmEntity idm;

    @ManyToOne
    @JoinColumn(name = "iuser",nullable = false)
    private UserEntity iuser;

    @Column(length = 2000 , nullable = false)
    private String msg;

}
