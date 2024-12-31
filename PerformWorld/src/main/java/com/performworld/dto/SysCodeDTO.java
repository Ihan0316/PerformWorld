package com.performworld.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysCodeDTO {
    private Long codeId;
    private String mainCode;
    private String subCode;
    private String code;
    private String codeCate;
    private String codeName;
}
