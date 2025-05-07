package com.example.backend.controller;

import com.example.backend.dto.MemberForm;
import com.example.backend.repository.MemberRepository;
import com.example.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 로그인 페이지로 이동
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * 회원가입 페이지로 이동
     */
    @GetMapping("/members/new")
    public String showSignupForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "signup";
    }

    /**
     * 회원가입
     */
    @PostMapping("/members")
    public String register(@Valid @ModelAttribute MemberForm form,
                           BindingResult result) {
        // TODO: 유효성 검사, 패스워드 일치 확인, DB 저장 등
        // 비밀번호 일치 여부 검증
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
        }

        try {
            memberService.register(form);
        } catch (IllegalStateException e) {
            result.rejectValue("email", "error.email", e.getMessage());
            return "members/new";
        }

        return "redirect:/login";
    }

}
