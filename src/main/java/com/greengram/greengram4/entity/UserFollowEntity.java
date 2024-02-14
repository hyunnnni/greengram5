package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_user_follow")
//복합키와 포링키를 거는 방식
public class UserFollowEntity extends CreatedAtEntity{

    @EmbeddedId
    private UserFollowIds userFollowIds;

    @ManyToOne
    @MapsId("fromIuser")//객체에 있는 이 멤버필드와 매칭하겠다
    @JoinColumn(name = "from_iuser", columnDefinition = "BIGINT UNSIGNED")
    private UserEntity fromUserEntity;

    @ManyToOne
    @MapsId("toIuser")//객체에 있는 이 멤버필드와 매칭하겠다
    @JoinColumn(name = "to_iuser", columnDefinition = "BIGINT UNSIGNED")
    private UserEntity toUserEntity;
}
