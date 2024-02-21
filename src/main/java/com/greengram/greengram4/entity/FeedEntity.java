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

    @ManyToOne//디폴트 (fetch = FetchType.EAGER) 이건 한 개만 가져올 때 사용
    @JoinColumn(name = "iuser", nullable = false)
    private UserEntity userEntity;

    @Column(length = 1000)
    private String contents;

    @Column(length = 30)
    private String location;

    @Builder.Default
    @ToString.Exclude//toString를 사용할 때 이 멤버필드는 빼고 스트링으로 찍으라는 애노테이션
    @OneToMany(mappedBy = "feedEntity",cascade = CascadeType.PERSIST)//양방향 (영속성 전이)
    //cascade = CascadeType.PERSIST 영속성을 주는 것
    //피드 저장을 할 때
    private List<FeedPicsEntity> feedPicsEntityList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "feedEntity")
    private List<FeedFavEntity> feedFavList = new ArrayList<>();

}
