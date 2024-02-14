package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_feed")
public class FeedEntity extends BaseEntity{

    @Id
    @Column(columnDefinition = "BIGINT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ifeed;

    @ManyToOne
    @JoinColumn(name = "iuser", nullable = false)
    private UserEntity iuser;

    @Column(length = 1000)
    private String contents;

    @Column(length = 30)
    private String location;
}
