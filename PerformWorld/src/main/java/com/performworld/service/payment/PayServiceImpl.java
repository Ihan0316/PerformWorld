package com.performworld.service.payment;

import com.performworld.dto.ticket.BookingDTO;
import com.performworld.repository.payment.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final PayRepository payRepository;

    // 예매 결제 등록
    @Override
    public void registPayment(BookingDTO bookingDTO) {
        payRepository.registPayment(bookingDTO);
    }
}
