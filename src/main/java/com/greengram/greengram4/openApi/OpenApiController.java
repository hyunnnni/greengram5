package com.greengram.greengram4.openApi;

import com.greengram.greengram4.openApi.model.ApartmentTransactionDetailDto;
import com.greengram.greengram4.openApi.model.ApartmentTransactionDetailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open")
public class OpenApiController {

    private final OpenApiService service;

    @GetMapping("apartment")
    public List<ApartmentTransactionDetailVo> getApartment(ApartmentTransactionDetailDto dto) throws Exception {
        return service.getApartmentTransactionList(dto);
    }
}
