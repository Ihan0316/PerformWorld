package com.performworld.dto;

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

    private List<MultipartFile> files;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
