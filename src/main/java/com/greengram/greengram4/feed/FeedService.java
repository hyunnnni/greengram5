package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.Const;
import com.greengram.greengram4.common.MyFileUtils;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.entity.*;
import com.greengram.greengram4.exception.FeedErrorCode;
import com.greengram.greengram4.exception.RestApiException;
import com.greengram.greengram4.feed.model.*;
import com.greengram.greengram4.security.AuthenticationFacade;
import com.greengram.greengram4.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
    private final FeedMapper mapper;
    private final FeedCommentMapper comMapper;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils mfu;
    private final FeedRepository repository;
    private final UserRepository userRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final FeedFavRepository feedFavRepository;
    @Transactional
    public FeedPicsInsDto postFeed(FeedInsDto dto){

        if (dto.getPics() == null) {
            throw new RestApiException(FeedErrorCode.PICS_THEN_ONE);
        }

        UserEntity userEntity = userRepository.getReferenceById((long) authenticationFacade.getLoginUserPk());


        FeedEntity entity = FeedEntity.builder()
                .userEntity(userEntity)
                .contents(dto.getContents())
                .location(dto.getLocation())
                .build();

        repository.save(entity);

        String target = "/feed/" + entity.getIfeed();

        FeedPicsInsDto pdto = new FeedPicsInsDto();
        for (MultipartFile file : dto.getPics()) {
            String saveFileNm = mfu.transferTo(file, target);
            pdto.getPics().add(saveFileNm);
        }
        pdto.setIfeed(entity.getIfeed());
        List<FeedPicsEntity> feedPicsEntityList = pdto.getPics()
                .stream()
                .map(item -> FeedPicsEntity.builder()
                        .feedEntity(entity)
                        .pic(item)
                        .build()).collect(Collectors.toList());
        entity.getFeedPicsEntityList().addAll(feedPicsEntityList);

        return pdto;
    }

    @Transactional
    public List<FeedSelVo> getFeedAll(FeedSelDto dto, Pageable pageable) {
        List<FeedEntity> feedEntityList = null;
        if (dto.getIsFavList() == 0 && dto.getTargetIuser() > 0) {
            UserEntity userEntity = new UserEntity();
            userEntity.setIuser((long) dto.getTargetIuser());
            feedEntityList = repository.findAllByUserEntityOrderByIfeedDesc(userEntity, pageable);
        }

        return feedEntityList == null ? new ArrayList<>()//펑션은 파라미터, 리턴이 있고 컨슈머는 파라미터만 있다 서큘라어쩌구 리턴만 있다
                : feedEntityList.stream().map(item -> {
                    FeedFavIds feedFavIds = new FeedFavIds();
                    feedFavIds.setIuser((long) authenticationFacade.getLoginUserPk());
                    feedFavIds.setIfeed(item.getIfeed());
                    int isFav = feedFavRepository.findById(feedFavIds).isPresent() ? 1 : 0;

                    List<String> piclist = item.getFeedPicsEntityList().stream().map(
                             feedPicsEntity -> feedPicsEntity.getPic()
                    ).collect(Collectors.toList());

                    List<FeedCommentSelVo> cmtlist =
                            feedCommentRepository.findAllTop4ByFeedEntity(item).stream().map(
                                    cmt -> FeedCommentSelVo.builder()
                                            .ifeedComment(cmt.getIfeedComment().intValue())
                                            .comment(cmt.getComment())
                                            .writerIuser(cmt.getUserEntity().getIuser().intValue())
                                            .writerPic(cmt.getUserEntity().getPic())
                                            .writerNm(cmt.getUserEntity().getNm())
                                            .createdAt(cmt.getCreatedAt().toString())
                                            .build()
                            ).collect(Collectors.toList());

            UserEntity userEntity = item.getUserEntity();
            int isMoreCom = 0;

            if(cmtlist.size() == 4){
                cmtlist.remove(4);
                isMoreCom = 1;
            }

            return FeedSelVo.builder()
                    .ifeed(item.getIfeed().intValue())
                    .contents(item.getContents())
                    .location(item.getLocation())
                    .createdAt(item.getCreatedAt().toString())
                    .writerIuser(userEntity.getIuser().intValue())
                    .writerNm(userEntity.getNm())
                    .writerPic(userEntity.getPic())
                    .comments(cmtlist)
                    .isMoreComment(isMoreCom)
                    .pics(piclist)
                    .isFav(isFav)
                    .build();
        }).collect(Collectors.toList());
    }
        /*System.out.println("!!!!!");
        list = mapper.feedSelAll(dto);

        FeedCommentSelDto fcDto = FeedCommentSelDto.builder()
                .startIdx(0)
                .rowCount(4)
                .build();

        for(FeedSelVo vo : list) {
            List<String> pics = mapper.feedSelPics(vo.getIfeed());
            vo.setPics(pics);

            fcDto.setIfeed(vo.getIfeed());
            List<FeedCommentSelVo> comments = comMapper.selFeedCommentAll(fcDto);
            vo.setComments(comments);

            if(comments.size() == 4) {
                vo.setIsMoreComment(1);
                comments.remove(comments.size() - 1);
            }
        }
        return list;*/

    public ResVo toggleFeedFav(FeedFavDto dto){
        int result = mapper.delFeedFav(dto);
        if(result == 1){
            return new ResVo(Const.FEED_FAV_DEL);
        }
        result = mapper.insFeedFav(dto);
        return new ResVo(Const.FEED_FAV_ADD);
    }

    public ResVo DelFeed(FeedDelDto dto) {

        int result = mapper.delComFavPics(dto);
        if(result == 1) {
            result = mapper.delFeed(dto);
            return new ResVo(result);
        }
        return new ResVo(result);
    }


}
