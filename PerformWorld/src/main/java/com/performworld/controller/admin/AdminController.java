package com.performworld.controller.admin;

import com.performworld.dto.tier.TierDTO;
import com.performworld.service.tier.TierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TierService tierService;

    // DB에서 모든 Tier 정보 가져오기
    @GetMapping("/userlist")
    public String getTierList(Model model) {
        List<TierDTO> tierDTOs = tierService.getAllTiers();  // TierService를 통해 모든 Tier 정보 가져오기
        model.addAttribute("tiers", tierDTOs);  // "tiers"라는 이름으로 Model에 추가
        return "admin/adminUserList";  // Thymeleaf 템플릿으로 이동
    }

    // Tier 등록 처리
    @PostMapping("/addTier")
    public String addTier(@RequestParam String tierName,
                          @RequestParam Long minSpent,
                          @RequestParam Long maxSpent,
                          @RequestParam Long discountRate) {
        // 새로운 TierDTO 객체 생성
        TierDTO newTier = new TierDTO(null, tierName, minSpent, maxSpent, discountRate);

        // TierService를 통해 새 Tier 등록
        tierService.addTier(newTier);

        // 성공적으로 추가된 후 Tier 목록 페이지로 리다이렉션
        return "redirect:/admin/adminUserlist";  // 새 Tier를 등록한 후 다시 Tier 목록으로 리다이렉트
    }
}
