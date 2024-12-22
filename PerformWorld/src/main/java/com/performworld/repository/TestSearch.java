package com.performworld.repository;

import com.performworld.domain.Test;
import com.performworld.dto.TestDTO;

import java.util.List;

public interface TestSearch {

    List<Test> getList(TestDTO testDTO);
}
