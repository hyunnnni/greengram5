package com.greengram.greengram4.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSigninDto {
    @JsonIgnore
    private String providerType;
    @Schema(example = "hong")
    private String uid;
    @Schema(example = "1234")
    private String upw;
    @JsonIgnore
    private int iuser;
}
