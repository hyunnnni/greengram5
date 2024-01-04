package com.greengram.greengram4;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@Import(CharEncodingConfig.class)
//@MockMvcConfig 위와 같은 기능을 한다 둘 중 뭘 사용해도 상관없다 한글깨짐 방지
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//랜덤한 포트를 이용해 테스트함
@AutoConfigureMockMvc
@Transactional //트랜잭션을 걸기 위한 애노테이션
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseIntegrationTest {//통합테스트 시 사용될 것들이 공통되니 상속시켜서 사용할 수 있게 베이스를 만든 것

    @Autowired
    protected MockMvc mvc;//신호전송 post맨으로 호출하는 느낌

    @Autowired
    protected ObjectMapper om; //컨트롤러 서비스 매퍼 다 실행하는 것
    //객체를 json으로 반대도 됨 클래스임 인터페이스 아님
    //ObjectMapper는 이미 빈등록이 된 클래스이므로 NEW할 필요없이 바로 가져와서 사용하는 게 가능
    //xml도 객체로 그 반대도 가능
}
