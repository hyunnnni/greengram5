package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)//일부분만 객체화를 하겠다
//스프링 컨테이너로 올라오게 해주는 것
@Import({FeedService.class, FeedCommentService.class})//그 중에서도 얘만 빈 등록 하겠다는 뜻
    //서비스만 객체화를 하려면 연결된 mapper도 등록이 되어야 한다
    //그때 @MockBean사용

class FeedServiceTest {

    @MockBean//마치 주소값이 있는 거 처럼 꾸며준다 실체는 없지만 있는 거처럼 만들어줌
    private FeedMapper mapper;
    @MockBean
    private FeedCommentMapper comMapper;

    @Autowired
    private FeedService service;

    @Autowired
    private FeedCommentService commentService;

    @Test
    void postFeed() {
        when(mapper.insFeed(any())).thenReturn(1); //가짜로 이 메소드에 any 어떠한 걸 보낼 것인데
        // 정해진 1이란 값을 리턴하라는 뜻
        when(mapper.insFeedPics(any())).thenReturn(3);


        FeedInsDto dto = new FeedInsDto();
        dto.setIfeed(100);
        dto.setPics(new ArrayList<>());
        //dto.getPics().add("aaaa");
        ResVo vo = service.postFeed(dto);
        assertEquals(dto.getIfeed(), vo.getResult());

        verify(mapper).insFeed(any());// 메소드를 실제로 호출했는지 확인
        verify(mapper).insFeedPics(any());// any 아무거나 보냈는데 mapper안 해당 메소드가 실제로 작동했는지 확인
        //두 번 호출하고 확인하면 에러 발생
        //값이 제대로 들어왔는지 확인은 할 수 없다 호출여부만 안다요~

    }

    @Test
    void getFeedAll() {
        FeedSelVo feedSelVo1 = new FeedSelVo();
        feedSelVo1.setIfeed(1);
        feedSelVo1.setContents("일번 feedSelVo");

        FeedSelVo feedSelVo2 = new FeedSelVo();
        feedSelVo2.setIfeed(2);
        feedSelVo2.setContents("이번 feedSelVo");

        List<FeedSelVo> list = new ArrayList<>();
        list.add(feedSelVo1);
        list.add(feedSelVo2);

        when(mapper.feedSelAll(any())).thenReturn(list);//이걸 사용하기 전 feedSelAll은 가짜로 객체화된 (목빈)
        //mapper의 메소드니깐 값을 주더라도 값은 빈 배열이었다
        //그러나 when을 사용해서 빈 배열이 아닌 채워진 배열을 넣어 서비스 메소드가 실행이 잘 굴러가고 검증도 할 수 있게 한것

        List<String> feed1Pics = Arrays.stream(new String[]{"a.jpg","d.jpg"}) .toList();

        List<String> feed2Pics = new ArrayList<>();
        feed2Pics.add("가.jpg");
        feed2Pics.add("나.jpg");

        List<List<String>> picsList = new ArrayList<>();//리스트에 리스트 담는 법
        picsList.add(feed1Pics);
        picsList.add(feed2Pics);

        List<String>[] picsarr = new List[2];//배열에 리스트 담는 법 둘 중 아무거나 사용해서 비교에 사용하면 됨
        picsarr[0] = feed1Pics;//리스트보단 속도가 빠르다
        picsarr[1] = feed2Pics;

        when(mapper.feedSelPics(1)).thenReturn(feed1Pics);
        when(mapper.feedSelPics(2)).thenReturn(feed2Pics);

        FeedCommentSelVo comVo1 = new FeedCommentSelVo();
        comVo1.setIfeedComment(1);
        comVo1.setWriterIuser(1);
        comVo1.setComment("뭘봐");

        FeedCommentSelVo comVo2 = new FeedCommentSelVo();
        comVo2.setIfeedComment(2);
        comVo2.setWriterIuser(2);
        comVo2.setComment("널봐");

        List<FeedCommentSelVo> comList1 = new ArrayList<>();
        comList1.add(comVo1);
        comList1.add(comVo2);

        FeedCommentSelVo comVo3 = new FeedCommentSelVo();
        comVo3.setIfeedComment(3);
        comVo3.setWriterIuser(1);
        comVo3.setComment("후르롹끼");

        FeedCommentSelVo comVo4 = new FeedCommentSelVo();
        comVo4.setIfeedComment(4);
        comVo4.setWriterIuser(2);
        comVo4.setComment("끼룰룽야");
        FeedCommentSelVo comVo5 = new FeedCommentSelVo();
        comVo4.setIfeedComment(5);
        comVo4.setWriterIuser(3);
        comVo4.setComment("케케");
        FeedCommentSelVo comVo6 = new FeedCommentSelVo();
        comVo4.setIfeedComment(5);
        comVo4.setWriterIuser(4);
        comVo4.setComment("랄라");

        List<FeedCommentSelVo> comList2 = new ArrayList<>();
        comList2.add(comVo3);
        comList2.add(comVo4);
        comList2.add(comVo5);
        comList2.add(comVo6);

        List<List<FeedCommentSelVo>> commentList = new ArrayList<>();
        commentList.add(comList1);
        commentList.add(comList2);

        FeedCommentSelDto fcDto = FeedCommentSelDto.builder()
                .ifeed(feedSelVo1.getIfeed())
                .startIdx(0)
                .rowCount(4)
                .build();

        FeedCommentSelDto fcDto2 = FeedCommentSelDto.builder()
                .ifeed(feedSelVo2.getIfeed())
                .startIdx(0)
                .rowCount(4)
                .build();//파라미터로 넘어가는 값이 로직에서 정해져 있다면 똑같이 만들어서 파라미터를 보내주어야 한다.

        when(comMapper.selFeedCommentAll(fcDto)).thenReturn(comList1);
        when(comMapper.selFeedCommentAll(fcDto2)).thenReturn(comList2);


        FeedSelDto dto = new FeedSelDto();
        List<FeedSelVo> result = service.getFeedAll(dto);

        assertEquals(list, result);

        for (int i = 0; i < list.size(); i++) {
            FeedSelVo vo = list.get(i);
            assertNotNull(vo.getPics());
            assertNotNull(vo.getComments());

            assertEquals(vo.getPics(), picsList.get(i));

            assertEquals(vo.getPics(), picsarr[i]);

            assertEquals(vo.getComments(), commentList.get(i));

            if(vo.getIfeed() == 1){
                assertEquals(0, vo.getIsMoreComment());
                assertEquals(2,vo.getComments().size());//삼항식도 됨~
                continue;
            }
            assertEquals(1, vo.getIsMoreComment());
            assertEquals(3,vo.getComments().size());
        }
    }

    @Test
    void toggleFeedFav() {
    }

    @Test
    void delFeed() {
    }
}