package com.performworld.service;

import com.performworld.dto.SysCodeDTO;

import java.util.List;

public interface SysService {

    List<SysCodeDTO> getSysList(String mainCode);
}
