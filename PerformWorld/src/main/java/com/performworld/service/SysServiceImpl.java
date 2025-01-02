package com.performworld.service;

import com.performworld.dto.SysCodeDTO;
import com.performworld.repository.systemcode.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SysServiceImpl implements SysService {

    private final SystemCodeRepository systemCodeRepository;

    // 시스템 코드 목록 조회
    @Override
    public List<SysCodeDTO> getSysList(String mainCode) {
        return systemCodeRepository.getSysList(mainCode);
    }
}
