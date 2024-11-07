package com.boorisoogeo.EatSmart.web.member;

import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller

@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/signup")
    public String addForm(Member member){
        return "members/addMemberForm";
    }

    @PostMapping("/signup")
    public String save(@Validated @ModelAttribute("member") Member member, BindingResult result, Model model){
        if(result.hasErrors()){
            return "members/addMemberForm";
        }
        if (!member.getPassword().equals(member.getConfirm_password())) {
            result.rejectValue("confirm_password", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "members/addMemberForm";
        }

        Member savedMember = memberRepository.save(member);
        log.info("savedMember={}",savedMember);
        return "redirect:/";
    }
}
