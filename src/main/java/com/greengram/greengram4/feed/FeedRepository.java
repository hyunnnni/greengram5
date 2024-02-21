package com.greengram.greengram4.feed;

import com.greengram.greengram4.entity.FeedEntity;
import com.greengram.greengram4.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntity, Long> , FeedQdslRepository{
    @EntityGraph(attributePaths = {"userEntity"})//셀렉할 때 같이 가져올 멤버필드 명을 적으면 됨
    //n+1해결 이걸 주게 되면 조인 걸어서 쿼리문이 한 개 날아가게 된다
    //원래 같으면 유저와 피드 테이블 각각 하나씩 셀렉해서 값을 가져온다
    //(fetch = FetchType.EAGER)처럼 즉시 로딩 시 가져오기 하기 위한..?
    List<FeedEntity> findAllByUserEntityOrderByIfeedDesc(UserEntity userEntity, Pageable pageable);
}
