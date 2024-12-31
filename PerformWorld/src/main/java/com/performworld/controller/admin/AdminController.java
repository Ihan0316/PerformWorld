package com.performworld.controller.admin;

import com.performworld.dto.admin.TierDTO;
import com.performworld.dto.user.UserDto;
import com.performworld.service.admin.TierService;
import com.performworld.service.admin.UserListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TierService tierService;
    private final UserListService userListService;  // UserListService 주입

    // Tier와 사용자 목록 조회
    @GetMapping("/userlist")
    public String getTierAndUserList(Model model) {
        // Tier 목록과 사용자 목록을 각각 가져옵니다.
        List<TierDTO> tierDTOs = tierService.getAllTiers();
        List<UserDto> userDtos = userListService.getAllUsers();  // UserDto 객체를 사용

        // Model에 Tier와 User 데이터를 추가합니다.
        model.addAttribute("tiers", tierDTOs);
        model.addAttribute("users", userDtos);  // UserDto 객체 목록을 모델에 추가

        return "admin/AdminUserList";  // 사용자와 Tier 목록을 표시할 뷰
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
}