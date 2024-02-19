package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_dm")//없을 시 클래스 명이 테이블 이름이 된다
public class DmEntity extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")//하드 코딩 이게 없다면 unsigned를 넣을 수 없다 그래서 unsigned를 넣기 위해 적었는데 적으려면 타입까지 다 적어줘야 한다
    //타입만 작성 가능
    private Long idm;//pk는 대부분 클래스 Long으로 사용함 integer도 가능

    @Column( length = 2000, name = "last_msg")//숫자일 때는 length가 의미 없다 name는 완전히 다른 이름일 때만 사용하면 됨 자동으로 띄어쓰기적용됨
    private String lastMsg;

    @LastModifiedDate
    @Column(name = "last_msg_at")
    private LocalDateTime lastMsgAt;
}
