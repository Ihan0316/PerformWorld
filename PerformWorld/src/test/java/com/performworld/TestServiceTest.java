package com.performworld;

import com.performworld.dto.TestDTO;
import com.performworld.service.TestService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class TestServiceTest {

    @Autowired
    private TestService testService;

    @Test
    public void insert() {
        IntStream.range(1, 101).forEach(i -> {
            TestDTO test = TestDTO.builder()
                    .name("test"+i)
                    .chkType(i%2==1)
                    .birth(LocalDateTime.now())
                    .address("busan")
                    .build();
            testService.insert(test);
        });
    }
}
