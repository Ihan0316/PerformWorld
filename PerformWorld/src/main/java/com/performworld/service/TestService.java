package com.performworld.service;

import com.performworld.dto.TestDTO;

import java.util.List;

public interface TestService {

    List<TestDTO> getList(TestDTO testDTO);

    Long insert(TestDTO testDTO);

    Long update(TestDTO testDTO);

    void delete(Long id);
}
