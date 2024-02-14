package com.greengram.greengram4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@Data
@Embeddable//복합키를 주는 객체지향적 방식
@EqualsAndHashCode
public class UserFollowIds implements Serializable {//복합키를 위한 객체
    private Long fromIuser;
    private Long toIuser;
}
