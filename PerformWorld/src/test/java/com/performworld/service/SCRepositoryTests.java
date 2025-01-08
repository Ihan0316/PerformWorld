package com.performworld.service;

import com.performworld.repository.board.FAQRepository;
import com.performworld.repository.board.NoticeRepository;
import com.performworld.repository.board.QnARepository;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class SCRepositoryTests {
    @Autowired
    private NoticeRepository noticeRepository;
    private FAQRepository faqRepository;
    private QnARepository qnARepository;

    @Test
    public void test() {
        log.info(noticeRepository.findAll());
    }

//    @Test
//    public void test2() {
//        log.info(faqRepository.findAll());
//    }
//
//    @Test
//    public void test3() {
//        log.info(qnARepository.findAll());
//    }
}
