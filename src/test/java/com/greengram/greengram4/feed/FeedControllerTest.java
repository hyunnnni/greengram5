package com.greengram.greengram4.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengram.greengram4.MockMvcConfig;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.FeedInsDto;
import com.greengram.greengram4.feed.model.FeedSelVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcConfig//MockMvcConfig 인터페이스 만들어서 사용한 애노테이션 한글 깨짐을 방지해줌 utf-8
//@Import(CharEncodingConfig.class) 위와 같은 것
@WebMvcTest({FeedController.class})//스프링 컨네이너 올려줌 빈 등록이 된다.
class FeedControllerTest {

    @Autowired
    private MockMvc mvc; // 신호 전송
    //실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체 만듦
    //mvc동작을 재현할 수 있다
    //주소값과 메소드를 날릴 수 있는 기능 (포스트맨같은?)

    @MockBean
    private FeedService service;

    @Autowired
    private ObjectMapper mapper;//객체를 JSON으로 JSON을 객체로 바꿔주는 기능

    @Test
    void postFeed() throws Exception{//예외를 처리함
        ResVo result = new ResVo(2);
        //when(service.postFeed(any())).thenReturn(result);
        given(service.postFeed(any())).willReturn(result);
        //테스트 시 given when then 총 세부분 나눠져 있다.
        //테스트 부분에 따라 이름이 다른 것 검증을 하기 위해 값을 세팅하는 과정들이 given
        //when은 실제로 실행을 돌리는 부분
        //then은 마지막으로 검증을 하는 부분 assertEquals 쓰는 부분

        FeedInsDto dto = new FeedInsDto();
        String json = mapper.writeValueAsString(dto);//자바 객체를 json형식으로 보이는 문자열로 만드는 메소드
        System.out.println("json"+ json);



        mvc.perform(//통신 중 요청을 전송하는 역할
                //ResultActions객체를 받는다 제공하는 메소드>> andExcpect
                MockMvcRequestBuilders
                        .post("/api/feed")//헤더 http /메소드를 결정할 수 있다(get, post,put,delete
                        .content(json)//헤더, 파라미터 안 내용물을 문자열로 바꿔준다.
                        .contentType(MediaType.APPLICATION_JSON)//바디부분 - 데이터를 json으로 날릴때 필수 보내는 타입은 json으로할거야
                //만약 이게 없다면 FORM데이터라고 생각해버린다(서버에 데이터를 날리는 형식 중 하나)
                //JSON과 다르다 그래서 JSON일 땐 꼭 적어야한다.
        )
                .andExpect(status().isOk())//status : 상태값, 통신 응답결과 ex)200
                .andExpect(content().string(mapper.writeValueAsString(result)))//result를 문자열로 바꾼 것
                //응답할 때 보낸 바디에 담긴 문자열과. 내가 기대하는 바디 문자열과 동일한가
                //리턴값을 검증하고 확인하는 메소드
                .andDo(print());//통신의 결과를 프린트
        //컨트롤러에서 제대로 ResVo를 제대로 받아서 그대로 리턴하는지를 확인하는 것

        verify(service).postFeed(any());
    }

    @Test
    void getFeedAll() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); //스프링 꺼 해쉬맵과 비슷?
        params.add("page","2");//값이 실제로 들어가진 않는다
        params.add("loginedIuser","4");

        List<FeedSelVo> vo = new ArrayList<>();
        FeedSelVo vo1 = new FeedSelVo();
        vo1.setIfeed(1);
        vo1.setContents("안녕하세요");

        FeedSelVo vo2 = new FeedSelVo();
        vo2.setIfeed(2);
        vo2.setContents("def");

        vo.add(vo1);
        vo.add(vo2);

        given(service.getFeedAll(any())).willReturn(vo);


        mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/feed")
                        .params((params))//쿼리스트링을 자동으로 만들어줌 이 메소드 사용하지 않고도 위의 api 뒤에 바로 적어주어도 됨
                //requestbody가 없다면 위의 post처럼 content로 넣어주지 않아도 통신은 가능
                //그냥 값을 넣고 싶어서 적은 params
                //메소드 호출에 또 메소드 호출하는 것(체이닝 기법,,?들었는데 까먹음)
                //RETURN THIS

        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(vo)))
                .andDo((print()));

        verify(service).getFeedAll(any());
    }

    @Test
    void toggleFeedFav() {
    }

    @Test
    void delFeed() throws Exception{
        ResVo result = new ResVo(3);
        given(service.DelFeed(any())).willReturn(result);

        MultiValueMap<String, String > param = new LinkedMultiValueMap<>();
        param.add("iuser","1" );
        param.add("ifeed","1" );

        mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/feed")
                        .params(param)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(result)))
                .andDo(print());

        verify(service).DelFeed(any());
    }

}