package com.boorisoogeo.EatSmart.web;

import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
@SessionAttributes(SessionConst.LOGIN_MEMBER)
public class HomeController {

    @GetMapping("/")
    public String homeBeforeLogin(@Login Member loginMember, Model model) {


        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "members/homeAfterLogin";
    }

    @GetMapping("/mypage")
    public String myPage(@Login Member loginMember, Model model){
        model.addAttribute("member", loginMember);

        if(loginMember == null){
            return "home";
        }

        return "members/myPage";
    }
}