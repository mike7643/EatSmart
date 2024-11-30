package com.boorisoogeo.EatSmart.web.member;

import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.repository.MemberRepository;
import com.boorisoogeo.EatSmart.service.MemberService;
import com.boorisoogeo.EatSmart.web.Login;
import com.boorisoogeo.EatSmart.web.dto.MemberUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/signup")
    public String createAccountForm(Model model) {
        model.addAttribute("member", new Member());
        return "members/addMemberForm";
    }

    @PostMapping("/signup")
    public String createAccount(@Validated @ModelAttribute("member") Member member, BindingResult result) {
        if (!member.getPassword().equals(member.getConfirmPassword())) {
            result.rejectValue( "confirmPassword",null,
                    "2차 확인용 패스워드가 일치하지 않습니다.");
        }

        if ((memberService.CheckDuplicateLoginId(member.getLoginId()))) {
            result.rejectValue("loginId",null,
                    "이미 가입된 아이디 입니다.");
        }
        if (result.hasErrors()) {
            return "members/addMemberForm";
        }

        Member savedMember = memberRepository.save(member);
        log.info("savedMember={}", savedMember);
        return "redirect:/";
    }

    @GetMapping("/updatememberinfo")
    public String updateMemberInfoForm(Model model) {
        model.addAttribute("memberUpdate", new MemberUpdateDto());
        return "members/memberUpdate";
    }

    @PostMapping("/updatememberinfo")
    public String UpdateMemberInfo(@Validated @ModelAttribute("memberUpdate") MemberUpdateDto memberUpdateDto,
                                   BindingResult result, @Login Member member, RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {

        if(result.hasErrors())
            return "members/memberUpdate";

        if (!memberUpdateDto.getPassword().equals(memberUpdateDto.getConfirmPassword())) {
            result.rejectValue( "confirmPassword",null,
                    "2차 확인용 패스워드가 일치하지 않습니다.");
        }

        memberService.update(member.getId(),memberUpdateDto);

        log.info("회원 정보 수정됨, [loginId -> {}", member.getLoginId());

        HttpSession session = request.getSession(false);
        redirectAttributes.addFlashAttribute("memberUpdateSessionOut", "회원 정보가 수정되었습니다. 재 로그인 하세요.");

        session.invalidate();
        log.info("session 종료");
        return "redirect:/login";
    }
}
