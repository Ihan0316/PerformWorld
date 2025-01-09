package com.performworld.controller.admin;

import com.performworld.dto.admin.SeatDTO;
import com.performworld.dto.admin.TierDTO;
import com.performworld.dto.user.UserDTO;
import com.performworld.service.admin.SeatService;
import com.performworld.service.admin.TierService;
import com.performworld.service.admin.UserListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminRestController {

    private final TierService tierService;
    private final UserListService userListService;
    private final SeatService seatService;

    // Tier 목록 가져오기
    @GetMapping("/getAllTiers")
    @ResponseBody
    public List<TierDTO> getAllTiers() {
        return tierService.getAllTiers();
    }

    // 사용자 목록 가져오기
    @GetMapping(value = "/getAllUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        return userListService.getAllUsers();
    }

    // 좌석 데이터 가져오기
    @GetMapping("/getAllSeats")
    @ResponseBody
    public List<SeatDTO> getAllSeats() {
        return seatService.getAllSeats();
    }

    // 티어 추가하기
    @PostMapping("/addTier")
    @ResponseBody
    public ResponseEntity<?> addTier(@Valid @RequestBody TierDTO tierDTO) {
        try {
            TierDTO savedTier = tierService.addTier(tierDTO);
            return ResponseEntity.ok(savedTier);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tier 등록에 실패했습니다.");
        }
    }

    // 사용자 삭제 처리
    @DeleteMapping("/user/{userId}")
    @ResponseBody
    public String deleteUser(@PathVariable String userId) {
        try {
            userListService.deleteUserById(userId);  // UserListService에서 사용자 삭제 메서드 호출
            return "사용자가 삭제되었습니다.";
        } catch (Exception e) {
            return "사용자 삭제에 실패했습니다.";
        }
    }

    // 섹션 수정
    @PutMapping("/section")
    public ResponseEntity<String> updateSectionPrice(@Valid @RequestBody SeatDTO seatDTO) {
        try {
            seatService.updateSectionPrice(seatDTO.getSection(), seatDTO.getPrice());
            return ResponseEntity.ok("섹션 가격 수정이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("섹션 가격 수정에 실패했습니다. 서버 오류 발생.");
        }
    }

    // 섹션 목록과 가격을 받아오기
    @GetMapping("/sections")
    public ResponseEntity<List<SeatDTO>> getAllSectionsWithPrices() {
        try {
            List<SeatDTO> sections = seatService.getAllSectionsWithPrices();
            return ResponseEntity.ok(sections);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }
}
