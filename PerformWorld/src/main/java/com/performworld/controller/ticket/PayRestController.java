package com.performworld.controller.ticket;

import com.performworld.dto.ticket.BookingDTO;
import com.performworld.service.payment.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayRestController {

    private final PayService payService;

    // 예매 결제 등록
    @PostMapping("/regist")
    public BookingDTO registPayment(@RequestBody BookingDTO bookingDTO) {
        log.info(bookingDTO);
        payService.registPayment(bookingDTO);
        return bookingDTO;
    }
}
