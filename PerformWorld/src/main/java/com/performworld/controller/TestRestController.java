package com.performworld.controller;

import com.performworld.dto.TestDTO;
import com.performworld.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestRestController {

    private final TestService testService;

    // 목록 조회
    @PostMapping("/getTestList")
    public List<TestDTO> getTestList(@RequestBody TestDTO testDTO) {
        log.info(testDTO);
        return testService.getList(testDTO);
    }

    // 등록
    @PostMapping("/registTest")
    public Long registTest(@RequestBody TestDTO testDTO) {
        log.info(testDTO);
        return testService.insert(testDTO);
    }
}
