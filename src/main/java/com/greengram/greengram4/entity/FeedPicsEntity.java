package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_feed_pics")
public class FeedPicsEntity extends CreatedAtEntity{

    @Id
    @Column(columnDefinition = "BIGINT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ifeedPics;

    @ManyToOne
    @JoinColumn(name = "ifeed", nullable = false)
    private FeedEntity ifeed;

    @Column(length = 2500)
    private String pic;
}
