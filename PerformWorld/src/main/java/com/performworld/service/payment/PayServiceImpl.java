package com.performworld.service.payment;

import com.performworld.domain.*;
import com.performworld.dto.ticket.BookingDTO;
import com.performworld.repository.admin.TierRepository;
import com.performworld.repository.event.BookRepository;
import com.performworld.repository.payment.PayRepository;
import com.performworld.repository.systemcode.SystemCodeRepository;
import com.performworld.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    @Value("${payment.api.secret}")
    private String portoneKey;

    private final BookRepository bookRepository;
    private final PayRepository payRepository;
    private final SystemCodeRepository systemCodeRepository;
    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final RestTemplate restTemplate;

    // 예매 결제 등록
    @Override
    public BookingDTO registPayment(BookingDTO bookingDTO) {
        // 해당 회차, 좌석의 예매 정보 존재하면 리턴
        if(!payRepository.checkBooking(bookingDTO).isEmpty()) {
            cancelPayment(bookingDTO.getPayment().getPaymentId());  // 결제 취소
            return BookingDTO.builder().resultCode(900).build();

        }
        // 결제 정보 api로 조회
        Map result = apiResult(bookingDTO.getPayment().getPaymentId());
        if(result == null) {
            return BookingDTO.builder().resultCode(800).build();
        }

        if ("PAID".equals(result.get("status"))) {
            // 결제 정보 정상 조회 시 저장
            SystemCode statusCode = systemCodeRepository.findByCode("Y").orElseThrow();
            List<Seat> seats = bookingDTO.getSeatIds().stream().map(id -> Seat.builder()
                            .seatId(id)
                            .build())
                    .collect(Collectors.toList());
            Booking booking = Booking.builder()
                    .user(User.builder().userId(bookingDTO.getUserId()).build())
                    .eventSchedule(EventSchedule.builder().scheduleId(bookingDTO.getScheduleId()).build())
                    .seats(seats)
                    .isDelivery(bookingDTO.isDelivery())
                    .totalPrice(bookingDTO.getTotalPrice())
                    .status(statusCode)
                    .build();
            Booking insertedBooking = bookRepository.save(booking);
            Payment payment = Payment.builder()
                    .paymentId(bookingDTO.getPayment().getPaymentId())
                    .booking(insertedBooking)
                    .paymentMethod((String) ((Map) ((Map) result.get("method")).get("card")).get("name"))
                    .paymentAmount((Integer) ((Map) result.get("amount")).get("total"))
                    .paymentDate(LocalDateTime.parse((String) result.get("paidAt"), DateTimeFormatter.ISO_DATE_TIME))
                    .email((String) ((Map) result.get("customer")).get("email"))
                    .status(statusCode)
                    .build();
            payRepository.save(payment);

            // 회원 소비금액 및 등급 업데이트
            User user = userRepository.findByUserId(bookingDTO.getUserId()).orElseThrow();
            user.chnTotalSpent(
                    user.getTotalSpent() + payment.getPaymentAmount()
                    , tierRepository.findAll()
            );
            userRepository.save(user);

            return BookingDTO.builder().bookingId(insertedBooking.getBookingId()).resultCode(200).build();
        }

        return BookingDTO.builder().resultCode(400).build();
    }

    // 예매 내역 조회
    @Override
    public List<BookingDTO> getBknList(BookingDTO bookingDTO) {
        return payRepository.getBknList(bookingDTO);
    }

    // 예매 상세 조회
    @Override
    public BookingDTO getBknInfo(Long bookingId) {
        return payRepository.getBknInfo(bookingId);
    }

    // 예매 취소
    @Override
    public BookingDTO cancelPayment(BookingDTO bookingDTO) {
        Optional<Booking> bookingOptional = bookRepository.findById(bookingDTO.getBookingId());

        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            Payment payment = payRepository.findByBooking_BookingId(bookingDTO.getBookingId()).orElseThrow();
            // 결제 취소
            cancelPayment(payment.getPaymentId());
            // 예매 및 결제 업데이트
            SystemCode cancelCode = systemCodeRepository.findByCode("N").orElseThrow();
            booking.chnStatus(cancelCode);
            bookRepository.save(booking);
            payment.chnStatus(cancelCode);
            payRepository.save(payment);
            // 회원 소비금액 및 등급 업데이트
            User user = userRepository.findByUserId(bookingDTO.getUserId()).orElseThrow();
            user.chnTotalSpent(
                    user.getTotalSpent() - payment.getPaymentAmount()
                    , tierRepository.findAll()
            );
            userRepository.save(user);
        }

        return BookingDTO.builder().resultCode(200).build();
    }



    private Map apiResult(String paymentId) {
        // PortOne 결제 조회 API URL
        String url = "https://api.portone.io/payments/" + paymentId;

        // Authorization 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + portoneKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // PortOne API에서 결제 내역 조회
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();

            } else {
                log.error("결제 조회 실패: {}", response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("결제 조회 중 예외 발생", e);
        }

        return null;
    }

    private Map cancelPayment(String paymentId) {
        // 환불 요청 URL
        String url = "https://api.portone.io/payments/" + paymentId + "/cancel";

        // 요청 본문
        String requestBody = "{\"reason\":\"테스트취소\"}";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "PortOne " + portoneKey);

        // HTTP 요청 생성
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // PortOne API로 환불 요청 보내기
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();

            } else {
                log.error("환불 요청 실패: {}", response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("환불 요청 중 예외 발생", e);
        }

        return null;
    }
}
