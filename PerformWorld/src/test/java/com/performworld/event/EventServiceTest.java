package com.performworld.event;

import com.performworld.service.event.EventService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@SpringBootTest
public class EventServiceTest {
    @Autowired
    private EventService eventService;

    @Test
    @Transactional
    public void testGetList(){
        log.info("테스트입니다:"+eventService.getAllEvents());
    }

}
