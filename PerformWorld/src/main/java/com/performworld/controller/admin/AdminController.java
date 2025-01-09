package com.performworld.controller.admin;

import com.performworld.domain.Tier;
import com.performworld.dto.admin.TierDTO;
import com.performworld.service.admin.TierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final TierService tierService;

    // userlist 이동
    @GetMapping("/userlist")
    public String showUserList(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "admin/AdminUserList";
    }
    // tierID를 이용해 tier 호출
    @GetMapping("/tier/{tierId}")
    @ResponseBody
    public ResponseEntity<TierDTO> getTier(@PathVariable Long tierId) {
        try {
            TierDTO tier = tierService.getTierById(tierId);
            return ResponseEntity.ok(tier);
        } catch (Exception e) {
            log.error("Error retrieving tier: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/getTiers")
    public ResponseEntity<List<TierDTO>> getTiers() {
        List<TierDTO> tiers = tierService.getAllTiers();  // DB에서 모든 Tier 목록 가져오기
        return ResponseEntity.ok(tiers);  // 모든 정보를 그대로 반환
    }

}
