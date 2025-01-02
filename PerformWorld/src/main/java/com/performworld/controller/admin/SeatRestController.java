package com.performworld.controller.admin;

import com.performworld.dto.admin.SeatDTO;
import com.performworld.service.admin.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seat")
@RequiredArgsConstructor
public class SeatRestController {

    private final SeatService seatService;

    // 좌석 목록 조회
    @PostMapping("/getSeatList")
    public List<SeatDTO> getSeatList() {
        return seatService.getAllSeats();
    }
}
