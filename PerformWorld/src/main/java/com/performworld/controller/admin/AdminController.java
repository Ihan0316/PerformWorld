package com.performworld.controller.admin;

import com.performworld.dto.user.UserDto;
import com.performworld.dto.tier.TierDTO;
import com.performworld.service.user.UserService;
import com.performworld.service.tier.TierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TierService tierService;
    private final UserService userService;

    // DB에서 모든 Tier 정보 가져오기
    @GetMapping("/userlist")
    public String getUserList(Model model) {
        // 회원 목록 조회
        List<UserDto> userDtos = userService.getAllUsers();
        // 모든 Tier 정보 가져오기
        List<TierDTO> tierDTOs = tierService.getAllTiers();

        // 모델에 회원과 Tier 정보를 추가
        model.addAttribute("users", userDtos);
        model.addAttribute("tiers", tierDTOs);
        return "admin/adminUserList";  // Thymeleaf 템플릿으로 이동
    }

    // 회원 상세 조회 페이지
    @GetMapping("/user/{userId}")
    public String getUserDetails(@PathVariable String userId, Model model) {
        // 특정 회원 정보 조회
        UserDto userDto = userService.getUserById(userId);
        model.addAttribute("user", userDto);
        return "admin/adminUserDetails";  // 회원 정보 상세 페이지로 이동
    }

    // 회원 정보 수정 페이지
    @GetMapping("/editUser/{userId}")
    public String editUser(@PathVariable String userId, Model model) {
        // 특정 회원 정보 조회
        UserDto userDto = userService.getUserById(userId);
        List<TierDTO> tierDTOs = tierService.getAllTiers(); // 회원의 Tier를 수정할 수 있도록 Tier 정보도 가져오기
        model.addAttribute("user", userDto);
        model.addAttribute("tiers", tierDTOs);
        return "admin/editUser";  // 회원 정보 수정 페이지로 이동
    }

    // 회원 정보 수정 처리
    @PostMapping("/editUser")
    public String updateUser(UserDto userDto) {
        // 회원 정보 수정
        userService.updateUser(userDto);
        return "redirect:/admin/userlist";  // 수정 후 회원 목록으로 리다이렉션
    }

    // 회원 삭제 처리
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam String userId) {
        // 회원 삭제
        userService.deleteUser(userId);
        return "redirect:/admin/userlist";  // 삭제 후 회원 목록으로 리다이렉션
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
        return "redirect:/admin/userlist";  // 새 Tier를 등록한 후 다시 Tier 목록으로 리다이렉트
    }
}