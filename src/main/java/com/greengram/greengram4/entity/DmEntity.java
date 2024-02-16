package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_dm")
public class DmEntity extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long idm;

    @Column( length = 2000, name = "last_msg")
    private String lastMsg;

    @LastModifiedDate
    @Column(name = "last_msg_at")
    private LocalDateTime lastMsgAt;
}
