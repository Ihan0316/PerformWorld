package com.performworld.service;

import com.performworld.domain.Test;
import com.performworld.dto.TestDTO;
import com.performworld.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final ModelMapper modelMapper;

    // 목록 조회
    @Override
    public List<TestDTO> getList(TestDTO testDTO) {
        return testRepository.getList(testDTO).stream()
                .map(entity -> modelMapper.map(entity, TestDTO.class))
                .collect(Collectors.toList());
    }

    // 등록
    @Override
    public Long insert(TestDTO testDTO) {
        return testRepository.save(modelMapper.map(testDTO, Test.class)).getId();
    }
}
