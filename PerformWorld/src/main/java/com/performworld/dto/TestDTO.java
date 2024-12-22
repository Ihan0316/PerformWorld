package com.performworld.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.performworld.config.DateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {
    private Long id;
    private String name;
    private boolean chkType;
    private LocalDateTime birth;
    private String address;
    private String filePath;
    // file
    private List<MultipartFile> files;
    // search
    private String srhName;
    private boolean srhChkType;
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime srhStrBirth;
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime srhEndBirth;
    private String srhAddress;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
