package com.greengram.greengram4.openApi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HospitalDto {
    @Schema(example = "1")
    private int page;
    @Schema(example = "100")
    private int size;
    @Schema(example = "안양시")
    private String sigunNm;
}
