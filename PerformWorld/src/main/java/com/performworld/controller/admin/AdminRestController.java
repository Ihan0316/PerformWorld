package com.performworld.controller.admin;

import com.performworld.dto.admin.PagingRequestDTO;
import com.performworld.dto.admin.PagingResponseDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminRestController {

    private final TierService tierService;
    private final UserListService userListService;
    private final SeatService seatService;

    @PostMapping("/getAllData")
    @ResponseBody
    public Map<String, Object> getTierAndUserList() {

        // 페이징 처리된 좌석 요청 데이터
        PagingRequestDTO pagingRequest = new PagingRequestDTO();
        pagingRequest.setPage(0);  // 처음에는 첫 번째 페이지 (혹은 클라이언트에서 페이지 번호를 받을 수 있음)
        pagingRequest.setSize(10); // 페이지 당 10개 항목

        // Tier 목록과 사용자 목록을 각각 가져옵니다.
        List<TierDTO> tierDTOs = tierService.getAllTiers();
        List<UserDTO> userDTOs = userListService.getAllUsers();
        PagingResponseDTO<SeatDTO> seatPagedResponse = seatService.getPagedSeats(pagingRequest);

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("tiers", tierDTOs);
        response.put("users", userDTOs);
        response.put("seats", seatPagedResponse.getContent()); // 페이징 처리된 좌석 목록

        return response;
    }

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

    // 페이징
    @PostMapping("/paged")
    public ResponseEntity<PagingResponseDTO<SeatDTO>> getPagedSeats(@RequestBody PagingRequestDTO request) {
        PagingResponseDTO<SeatDTO> response = seatService.getPagedSeats(request);
        return ResponseEntity.ok(response);
    }

    // tier 수정
    @PutMapping("/tier/{tierId}")
    @ResponseBody
    public ResponseEntity<?> updateTier(
            @PathVariable Long tierId,
            @Valid @RequestBody TierDTO tierDTO) {
        try {
            TierDTO updatedTier = tierService.updateTier(tierId, tierDTO);
            return ResponseEntity.ok(updatedTier);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating tier: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Tier 수정에 실패했습니다.");
        }
    }

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