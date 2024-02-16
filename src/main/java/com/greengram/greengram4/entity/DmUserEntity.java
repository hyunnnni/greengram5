package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_dm_user")
public class DmUserEntity {

    @EmbeddedId
    private DmUserIds dmUserIds;

    @ManyToOne
    @MapsId("idm")
    @JoinColumn(name = "idm", columnDefinition = "BIGINT UNSIGNED")
    private DmEntity idm;

    @ManyToOne
    @MapsId("iuser")
    @JoinColumn(name = "iuser", columnDefinition = "BIGINT UNSIGNED")
    private UserEntity iuser;
}
