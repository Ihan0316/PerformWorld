package com.performworld.service.admin;

import com.performworld.domain.Seat;
import com.performworld.dto.admin.SeatDTO;
import com.performworld.repository.admin.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    // Seat 엔티티를 SeatDTO로 변환하는 메서드
    private SeatDTO convertToDTO(Seat seat) {
        // 수동으로 변환
        return new SeatDTO(
                seat.getSeatId(),        // Seat ID
                seat.getSection(),       // 섹션
                seat.getPrice()          // 가격
        );
    }

    // 섹션 가격 수정
    @Override
    @Transactional
    public void updateSectionPrice(String section, Long price) {
        seatRepository.updatePriceBySection(section, price);
    }


    @Override
    public List<SeatDTO> getAllSectionsWithPrices() {
        List<Object[]> result = seatRepository.findDistinctSectionsAndPrices();
        List<SeatDTO> seatDTOList = new ArrayList<>();

        for (Object[] row : result) {
            // 섹션과 가격이 올바르게 반환되는지 확인
            String section = (String) row[0];
            Long price = (Long) row[1];  // 이 부분은 가격이 Long으로 반환된다고 가정
            seatDTOList.add(new SeatDTO(null, section, price));  // seatId는 필요없으면 null로 처리
        }

        if (seatDTOList.isEmpty()) {
            // 빈 결과 처리
            System.out.println("No sections found.");
        }

        return seatDTOList;
    }
}