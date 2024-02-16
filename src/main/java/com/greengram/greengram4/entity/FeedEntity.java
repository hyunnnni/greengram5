package com.greengram.greengram4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Builder.Default
    @ToString.Exclude//toString를 사용할 때 이 멤버필드는 빼고 스트링으로 찍으라는 애노테이션
    @OneToMany(mappedBy = "ifeed",cascade = CascadeType.PERSIST)//양방향 전이
    private List<FeedPicsEntity> feedPicsEntityList = new ArrayList<>();

}
