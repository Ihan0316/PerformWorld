package com.performworld.service.admin;

import com.performworld.domain.Seat;
import com.performworld.dto.admin.PagingRequestDTO;
import com.performworld.dto.admin.PagingResponseDTO;
import com.performworld.dto.admin.SeatDTO;
import com.performworld.repository.admin.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<SeatDTO> getAllSeats() {
        // Retrieve all seats and map to SeatDTO
        List<Seat> seats = seatRepository.findAll(Sort.by(Sort.Order.asc("seatId")));
        return seats.stream()
                .map(seat -> new SeatDTO(seat.getSeatId(), seat.getSection(), seat.getPrice()))
                .collect(Collectors.toList());
    }

    // 페이징 처리된 좌석 조회
    @Override
    public PagingResponseDTO<SeatDTO> getPagedSeats(PagingRequestDTO request) {
        // PageRequest를 사용하여 페이징 처리
        Page<Seat> seatPage = seatRepository.findAll(PageRequest.of(request.getPage(), request.getSize()));
        // Page 객체에서 content만 추출하여 DTO로 변환
        List<SeatDTO> seatDTOList = seatPage.getContent().stream()
                .map(this::convertToDTO)  // DTO 변환을 위한 메서드 호출
                .collect(Collectors.toList());
        // PagingResponseDTO로 반환
        return new PagingResponseDTO<>(
                seatDTOList,
                seatPage.getTotalElements(),  // 전체 데이터 개수
                seatPage.getTotalPages(),     // 전체 페이지 개수
                seatPage.getNumber()          // 현재 페이지 번호
        );
    }

    // Seat 엔티티를 SeatDTO로 변환하는 메서드
    private SeatDTO convertToDTO(Seat seat) {
        // 수동으로 변환
        return new SeatDTO(
                seat.getSeatId(),        // Seat ID
                seat.getSection(),       // 섹션
                seat.getPrice()          // 가격
        );
    }
}