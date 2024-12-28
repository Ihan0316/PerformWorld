package com.performworld.controller;

import com.performworld.dto.SysCodeDTO;
import com.performworld.service.SysService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/sys")
@RequiredArgsConstructor
public class SysRestController {

    private final SysService sysService;

    @PostMapping("/getList")
    public List<SysCodeDTO> getSysList(@RequestBody SysCodeDTO sysCodeDTO) {
        return sysService.getSysList(sysCodeDTO.getMainCode());
    }
}
