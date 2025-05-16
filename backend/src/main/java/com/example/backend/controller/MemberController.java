package com.example.backend.controller;

import com.example.backend.config.JwtUtil;
import com.example.backend.dto.CustomUser;
import com.example.backend.dto.LoginForm;
import com.example.backend.dto.MemberForm;
import com.example.backend.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager; // Spring Security가 제공하는 인증 처리 도구
    private final JwtUtil jwtUtil;

    /**
     * 로그인 페이지로 이동
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            // 아이디/비밀번호 인증객체 생성
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword())
            );

            // JWT 토큰을 생성
            String token = jwtUtil.createToken(auth);

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1시간
            response.addCookie(cookie);

            return "redirect:/";  // 로그인 성공 후 홈으로 이동

        } catch (Exception e) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login";        }

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

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        SecurityContextHolder.clearContext();

        return "redirect:/login?logout";
    }

    /**
     * 로그인 상태 확인
     */
    @GetMapping("/api/auth/check")
    public ResponseEntity<?> checkAuth(@AuthenticationPrincipal CustomUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }
}
