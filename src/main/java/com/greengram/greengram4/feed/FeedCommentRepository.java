package com.greengram.greengram4.feed;

import com.greengram.greengram4.entity.FeedCommentEntity;
import com.greengram.greengram4.entity.FeedEntity;
import com.greengram.greengram4.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedCommentRepository extends JpaRepository<FeedCommentEntity, Long> {
    @EntityGraph(attributePaths = {"userEntity"})//처음에 조회 시 가져올 멤버필드 명(n+1해결
    List<FeedCommentEntity> findAllTop4ByFeedEntity(FeedEntity feedEntity);
    //limit = top 위에서 4개
}
