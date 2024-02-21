package com.greengram.greengram4.feed;

import com.greengram.greengram4.entity.FeedEntity;
import com.greengram.greengram4.feed.model.FeedSelVo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import static com.greengram.greengram4.entity.QFeedEntity.feedEntity;



import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class FeedQdslRepositoryImpl implements FeedQdslRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FeedSelVo> selFeedAll(long loginIuser, long targetIuser, Pageable pageable) {
        List<FeedEntity> list = jpaQueryFactory.select(feedEntity)
                .from(feedEntity)
                .where(whereTargetUser(targetIuser))// 이 안의 ,는 and 조건으로 적용된다
                .join(feedEntity.feedPicsEntityList).fetchJoin()
                .orderBy(feedEntity.ifeed.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return list.stream().map(item ->
            FeedSelVo.builder()
                    .ifeed(item.getIfeed().intValue())
                    .writerIuser(item.getUserEntity().getIuser().intValue())
                    .writerNm(item.getUserEntity().getNm())
                    .writerPic(item.getUserEntity().getPic())
                    .createdAt(item.getCreatedAt().toString())
                    .location(item.getLocation())
                    .contents(item.getContents())
                    .pics(item.getFeedPicsEntityList().stream().map(pic ->
                            pic.getPic()).collect(Collectors.toList()))
                    .isFav(item.getFeedFavList().stream().anyMatch(fav ->
                            fav.getUserEntity().getIuser() == loginIuser) ? 1 : 0)
                    .build()).collect(Collectors.toList());
    }

    private BooleanExpression whereTargetUser(long targetIuser){//target이 있는지 없는지 체크 target이 없다면 0이라면 null이고
        //아니라면 feedEntity.userEntity.iuser.eq(targetIuser) 이 조건을 넣어준다
        //eq 이퀄스 그래서 target이 0 이라면 where절이 쿼리문에 안 들어가게 된다
        return targetIuser == 0 ? null : feedEntity.userEntity.iuser.eq(targetIuser);
    }
}
