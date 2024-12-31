package com.performworld.controller.admin;

import com.performworld.dto.admin.SeatDTO;
import com.performworld.dto.admin.TierDTO;
import com.performworld.dto.user.UserDto;
import com.performworld.service.admin.SeatService;
import com.performworld.service.admin.TierService;
import com.performworld.service.admin.UserListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final TierService tierService;
    private final UserListService userListService;
    private final SeatService seatService;

    // Tier, 사용자, 좌석 목록 조회
    @GetMapping("/userlist")
    public String getTierAndUserList(Model model) {
        // Tier 목록과 사용자 목록을 각각 가져옵니다.
        List<TierDTO> tierDTOs = tierService.getAllTiers();
        List<UserDto> userDtos = userListService.getAllUsers();  // UserDto 객체를 사용
        List<SeatDTO> seats = seatService.getAllSeats(); // 좌석 목록 추가

        // Model에 Tier, User, Seat 데이터를 추가합니다.
        model.addAttribute("tiers", tierDTOs);
        model.addAttribute("users", userDtos);  // UserDto 객체 목록을 모델에 추가
        model.addAttribute("seats", seats);    // 좌석 목록 추가

        return "admin/AdminUserList";
    }

    // Tier 등록 처리
    @PostMapping("/addTier")
    @ResponseBody
    public TierDTO addTier(@RequestParam String tierName,
                           @RequestParam Long minSpent,
                           @RequestParam Long maxSpent,
                           @RequestParam Long discountRate) {
        TierDTO newTier = new TierDTO(null, tierName, minSpent, maxSpent, discountRate);
        return tierService.addTier(newTier);  // Tier 등록 서비스 호출
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