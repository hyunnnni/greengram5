package com.greengram.greengram4.feed;

import com.greengram.greengram4.feed.model.FeedDelDto;
import com.greengram.greengram4.feed.model.FeedFavDto;
import com.greengram.greengram4.feed.model.FeedInsDto;
import com.greengram.greengram4.feed.model.FeedInsPicsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import 되어 있으니 따로 클래스 이름 적고 메소드 호출 안해도 된다.
@MybatisTest//mybatis를 테스트한다. 스프링을 기동시킨다 dao만 연관되어 빈등록을 해달라 .xml만
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedMapperTest {

    @Autowired
    private FeedMapper mapper;
    //주소값을 달라고 한다.
    //싱글톤
    //---------------------fav------------------------
    @Test
    public void insFeedFav(){
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(1);
        dto.setIuser(2);

        List<FeedFavDto> result = mapper.selFeedFavForTest(dto);
        assertEquals(0, result.size());

        int affectedRows1 = mapper.insFeedFav(dto);
        assertEquals(1, affectedRows1,"첫 번째 테스트");//테스트 실패 시 위치를 알 수 있음

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        assertEquals(1, result2.size());
    }

    @Test
    public void delFeedFav(){
        FeedFavDto dto = new FeedFavDto();
        dto.setIuser(8);
        dto.setIfeed(110);

        List<FeedFavDto> result = mapper.selFeedFavForTest(dto);
        assertEquals(1, result.size());

        int affectedRows1 = mapper.delFeedFav(dto);
        assertEquals(1, affectedRows1);

        int affectedRows2 = mapper.delFeedFav(dto);
        assertEquals(0, affectedRows2);

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        assertEquals(0, result2.size());
    }

    @Test
    public void delFeedFavAll(){

        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(1);
        List<FeedFavDto> result = mapper.selFeedFavForTest(dto);

        int delAffectedRows = mapper.delFeedFavAll(dto.getIfeed());
        assertEquals(result.size(), delAffectedRows);

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        assertEquals(0, result2.size());
    }

    //-----------------pics------------------------

    private FeedInsPicsDto dto;
    private FeedInsDto ddto;
    public FeedMapperTest() {
        List<String> pics = new ArrayList();
        pics.add("a.jpg");
        pics.add("b.jpg");

        this.dto = FeedInsPicsDto.builder()
                .ifeed(110)
                .pics(pics)
                .build();

    }

    @BeforeEach
    public void beforeEach(){
        FeedDelDto delDto = new FeedDelDto();
        delDto.setIfeed(this.dto.getIfeed());
        delDto.setIuser(12);
        int affectedRows = mapper.delComFavPics(delDto);
        System.out.println("delRows : " + affectedRows);
    }

    @Test
    public void insFeedPics(){

        List<String> result = mapper.feedSelPics(dto.getIfeed());
        assertEquals(0, result.size());

        int result2 = mapper.insFeedPics(ddto);
        assertEquals(dto.getPics().size(),result2);

        List<String> result3 = mapper.feedSelPics(dto.getIfeed());
        assertEquals(dto.getPics().size(),result3.size());

        for(int i = 0; i == dto.getPics().size(); i++){
            assertEquals(dto.getPics().get(i), result3.get(i));
        }

    }

    public void feedSelPics(){

    }

    public void delComFavPics(){

    }
}