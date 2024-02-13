package com.greengram.greengram4.dm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.dm.model.*;
import com.greengram.greengram4.user.UserMapper;
import com.greengram.greengram4.user.model.UserSelEntity;
import com.greengram.greengram4.user.model.UserSigninDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DmService {
    private final DmMapper mapper;
    private final UserMapper userMapper;
    private final ObjectMapper objMapper;

    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dto){

        return mapper.selDmMsgAll(dto);
    }

    public List<DmSelVo> getDmAll(DmSelDto dto){
        return mapper.selDmAll(dto);
    }


    public ResVo delDmMsg(DmMsgDelDto dto){
        int delAffectedRows = mapper.delDmMsg(dto);
        if(delAffectedRows == 1) {
            int updAffectedRows = mapper.updDmLastMsgAfterDelByLastMsg(dto);
        }
        return new ResVo(delAffectedRows);

    }


    public DmSelVo postDm(DmInsDto dto) {
        Integer result = mapper.selCheckDm(dto);

        if (result != null) {
            return null;
/*            DmSelDto sdto = new DmSelDto();
            sdto.setLoginedIuser(dto.getLoginedIuser());
            List<DmSelVo> voList = mapper.selDmAll(sdto);
            log.info("service postDm voList = {}", voList);
            for(DmSelVo vo : voList){
                if(vo.getOtherPersonIuser() == dto.getOtherPersonIuser()){
                    return vo;
                }
            }*/
        }

        mapper.insMakeDm(dto);
        mapper.insMakeDmUser(dto);

        UserSigninDto sdto = UserSigninDto.builder()
                .iuser(dto.getOtherPersonIuser())
                .build();
        UserSelEntity entity = userMapper.selUser(sdto);//여기도 null이 들어올 수 있다 예외처리,,,


        return DmSelVo.builder()
                .idm(dto.getIdm())
                .otherPersonIuser(entity.getIuser())
                .otherPersonNm(entity.getNm())
                .otherPersonPic(entity.getPic())
                .build();
    }
    public ResVo postDmMsg(DmMsgInsDto dto) {
        int insAffectedRows = mapper.insDmMsg(dto);
        //last msg update
        if(insAffectedRows == 1) {
            int updAffectedRows = mapper.updDmLastMsg(dto);
        }
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜 구하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 정의
        String createdAt = now.format(formatter); // 포맷 적용

        //상대방의 firebaseToken값 필요. 나의 pic, iuser값 필요.
        UserSelEntity otherPerson = mapper.selOtherPersonByLoginUser(dto);

        try {

            if(otherPerson.getFirebaseToken() != null) {
                DmMsgPushVo pushVo = new DmMsgPushVo();
                pushVo.setIdm(dto.getIdm());
                pushVo.setSeq(dto.getSeq());
                pushVo.setWriterIuser(dto.getLoginedIuser());
                pushVo.setWriterPic(dto.getLoginedPic());
                pushVo.setMsg(dto.getMsg());
                pushVo.setCreatedAt(createdAt);

                //object to json 오브젝트를 제이슨으로 변경해주는 것이다
                //객체 안에 담긴 것을 문자열로 바꿔서 보내야 한다 객체 주소값으로 푸시를 날릴 순 없다
                String body = objMapper.writeValueAsString(pushVo);//바꿔주는 메소드
                log.info("body: {}", body);
                Notification noti = Notification.builder()
                        .setTitle("dm")
                        .setBody(body)
                        .build();

                Message message = Message.builder()
                        .setToken(otherPerson.getFirebaseToken())

                        .setNotification(noti)
                        .build();

                FirebaseMessaging.getInstance().sendAsync(message);//getInstance는 싱글톤 객체생성 common의 firebase에서 함
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResVo(dto.getSeq());
    }
}
