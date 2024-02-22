package com.greengram.greengram4.feed;

import com.greengram.greengram4.entity.FeedEntity;
import com.greengram.greengram4.entity.FeedFavEntity;
import com.greengram.greengram4.entity.FeedPicsEntity;
import com.greengram.greengram4.feed.model.FeedSelDto;
import com.greengram.greengram4.feed.model.FeedSelVo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import static com.greengram.greengram4.entity.QFeedEntity.feedEntity;
import static com.greengram.greengram4.entity.QFeedFavEntity.feedFavEntity;
import static com.greengram.greengram4.entity.QFeedPicsEntity.feedPicsEntity;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class FeedQdslRepositoryImpl implements FeedQdslRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FeedEntity> selFeedAll(FeedSelDto dto, Pageable pageable) {
        JPAQuery<FeedEntity> jpaQuery = jpaQueryFactory.select(feedEntity)
                .from(feedEntity)
                .join(feedEntity.userEntity).fetchJoin()//fetchJoin이 없다면 lazy를 준 해당 user정보셀렉이 3번 날아간다 join을 쓰던말던 user정보는 다 날아간다
                //1:1 상황이라 한 번에 가져와도 됨 유저 정보를 셀렉한번으로 다 가져와서 리턴하기 위한 방식으로 join을 넣었다?
                .where(whereTargetUser(dto.getTargetIuser()))// 이 안의 ,는 and 조건으로 적용된다
                .orderBy(feedEntity.ifeed.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
                //.fetch(); 일반 객체로 리턴을 받을 때 필요한 메소드
        //jpaQuery를 사용한다면 아래처럼 조건을 달고 조건추가가 더 가능해진다.

        if(dto.getIsFavList() == 1){
            jpaQuery.join(feedFavEntity).on(feedEntity.ifeed.eq(feedFavEntity.feedEntity.ifeed)
            ,feedFavEntity.userEntity.iuser.eq(dto.getLoginedIuser()));
        } else {
            jpaQuery.where(whereTargetUser(dto.getTargetIuser()));
        }

        /*return list.stream().map(item ->
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
                            fav.getUserEntity().getIuser() == dto.getLoginedIuser()) ? 1 : 0)
                    .build()).collect(Collectors.toList()); 유저 정보를 다시 셀렉해서 보낼 때 필요했던 방식*/

        return jpaQuery.fetch();
    }
    private BooleanExpression whereTargetUser(long targetIuser){//target이 있는지 없는지 체크 target이 없다면 0이라면 null이고
        //아니라면 feedEntity.userEntity.iuser.eq(targetIuser) 이 조건을 넣어준다
        //eq 이퀄스 그래서 target이 0 이라면 where절이 쿼리문에 안 들어가게 된다
        return targetIuser == 0 ? null : feedEntity.userEntity.iuser.eq(targetIuser);
    }
    @Override
    public List<FeedPicsEntity> selFeedPicsAll(List<FeedEntity> feedEntityList) {
        return jpaQueryFactory.select(Projections.fields(FeedPicsEntity.class
                        , feedPicsEntity.feedEntity, feedPicsEntity.pic))
                .from(feedPicsEntity)
                .where(feedPicsEntity.feedEntity.in(feedEntityList))
                .fetch();
    }

    @Override
    public List<FeedFavEntity> selFeedFavAllByME(List<FeedEntity> feedEntityList, long loginIuser) {
        return jpaQueryFactory.select(Projections.fields(FeedFavEntity.class,
                        feedFavEntity.feedEntity))
                .from(feedFavEntity)
                .where(feedFavEntity.feedEntity.in(feedEntityList),
                        feedFavEntity.userEntity.iuser.eq(loginIuser))
                                .fetch();
    }

}
