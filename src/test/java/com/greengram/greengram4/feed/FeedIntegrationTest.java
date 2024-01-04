package com.greengram.greengram4.feed;

import com.greengram.greengram4.BaseIntegrationTest;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.FeedInsDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedIntegrationTest extends BaseIntegrationTest {//피드 통합테스트 클래스

    @Test
    //@Rollback(false) db에 저장이 되는지 안되는지 정할 수 있는 애노테이션 true는 저장됨 false는 롤백이 됨
    public void postFeed() throws Exception{

        FeedInsDto dto = new FeedInsDto();
        dto.setIuser(2);
        dto.setContents("통합 테스트 작업 중");
        dto.setLocation("그린컴퓨터학원");

        List<String> pics = new ArrayList<>();
        pics.add("https://photo.newsen.com/news_photo/2023/01/16/202301161022545510_1.jpg");
        pics.add("https://dispatch.cdnser.be/cms-content/uploads/2023/04/24/3d7ddd63-f6f5-468d-b0b2-3c3b806443f5.jpg");

        dto.setPics(pics);


        String json = om.writeValueAsString(dto);
        System.out.println("json"+ json);


        MvcResult mr = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/feed")
                                .contentType(MediaType.APPLICATION_JSON)//json형태로 날리기
                                .content(json)//body에 담았다
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = mr.getResponse().getContentAsString();
        //{ result : 1 } << 이런 형태로 넘어오며 그걸 String으로 변환해서 content로 넣어준다.
        ResVo vo = om.readValue(content, ResVo.class);
        //ResVo를 문자열로 표현된 것을 다시 ResVo 객체로 바꾸어주는 메소드
        //( 문자열, 바꾸고자하는 객체 ) >> 사용하기 편해진다
        //ResVo에 기본생성자를 넣어주어야 바꿔진다
        assertEquals(true, vo.getResult() > 0);//연산을 사용할 때 이렇게 사용해 비교하기
    }

    @Test
    @Rollback(false)
    public void delFeed() throws Exception{

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ifeed", "7");
        params.add("iuser", "1");

        int ifeed = 9;
        int iuser = 2;

        MvcResult mr = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/feed?ifeed={ifeed}&iuser={iuser}", ifeed, iuser)// ?ifeed=7&iuser=3 쿼리로 작성도 되고
                        //.params(params) 파람을 사용해도 되며 위의 방식대로 하여도 된다
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = mr.getResponse().getContentAsString();
        ResVo vo = om.readValue(content, ResVo.class);
        assertEquals(1,vo.getResult());
    }
}
