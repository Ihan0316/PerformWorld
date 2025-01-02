package com.performworld.controller.admin;

import com.performworld.dto.admin.SeatDTO;
import com.performworld.dto.admin.TierDTO;
import com.performworld.dto.user.UserDTO;
import com.performworld.repository.admin.TierRepository;
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
    private final TierRepository tierRepository;

    // 모든 데이터 불러오기
    @PostMapping("/getAllData")
    @ResponseBody
    public Map<String, Object> getTierAndUserList(@RequestBody Map<String, Object> requestData) {
        // 클라이언트에서 요청한 파라미터를 사용할 수 있음
        String tierFilter = (String) requestData.get("tierFilter");  // 예시: 티어 필터링
        log.info("Received tierFilter: {}", tierFilter);

        // Tier 목록과 사용자 목록을 각각 가져옵니다.
        List<TierDTO> tierDTOs = tierService.getAllTiers();
        List<UserDTO> userDTOs = userListService.getAllUsers();  // UserDto 객체를 사용
        List<SeatDTO> seats = seatService.getAllSeats(); // 좌석 목록 추가

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("tiers", tierDTOs);
        response.put("users", userDTOs);  // UserDto 객체 목록
        response.put("seats", seats);    // 좌석 목록

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
}