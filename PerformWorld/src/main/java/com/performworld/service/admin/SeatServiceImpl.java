package com.performworld.service.admin;

import com.performworld.domain.Seat;
import com.performworld.dto.admin.SeatDTO;
import com.performworld.repository.admin.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}