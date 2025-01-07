package com.performworld.controller.ticket;

import com.performworld.dto.ticket.BookingDTO;
import com.performworld.service.payment.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return payService.registPayment(bookingDTO);
    }

    // 예매 내역 조회
    @PostMapping("/getBknList")
    public List<BookingDTO> getBknList(@RequestBody BookingDTO bookingDTO) {
        return payService.getBknList(bookingDTO);
    }

    // 예매 상세 조회
    @PostMapping("/getBknInfo")
    public BookingDTO getBknInfo(@RequestBody BookingDTO bookingDTO) {
        return payService.getBknInfo(bookingDTO.getBookingId());
    }

    // 예매 취소 (결제 취소)
    @PostMapping("/cancel")
    public BookingDTO cancelPayment(@RequestBody BookingDTO bookingDTO) {
        return payService.cancelPayment(bookingDTO);
    }
}
