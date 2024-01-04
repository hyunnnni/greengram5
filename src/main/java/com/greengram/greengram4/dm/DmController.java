package com.greengram.greengram4.dm;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.dm.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class DmController {
    private final DmService service;

    //page, fromiuser, toiuser
    //page가 1이면 방이 있었을 수도 없었을 수도 있는 2가지 경우를 가진다
    //그래서 page가 2 이상이면 방이 있는지 없는지 체크를 하지 않는다 바로 대화 리스트 응답
    //page가 1이면 있는지 없는지 체크 후 없으면 방을 만들고 빈 대화 리스트(사이즈가 0)를 방이 있다면 대화 리스트를 응답
    @Operation(summary = "dm 메시지 불러오기")
    @GetMapping("/msg")
    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dto){
        log.info("dto : {}", dto);
        return service.getMsgAll(dto);

    }
    @GetMapping
    public List<DmSelVo> getDmAll(DmSelDto dto){
        log.info("dto = {}", dto);
        return service.getDmAll(dto);
    }

    @PostMapping("/msg")
    public ResVo postDmMsg(@RequestBody DmMsgInsDto dto){
        return service.postDmMsg(dto);
    }

    @DeleteMapping("/msg")
    public ResVo delDmMsg(DmMsgDelDto dto){
        return service.delDmMsg(dto);
    }
    @PostMapping
    public DmSelVo postDm(@RequestBody DmInsDto dto){
        return service.postDm(dto);
    }
}
