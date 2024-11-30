package com.boorisoogeo.EatSmart.service;

import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.repository.MemberRepository;
import com.boorisoogeo.EatSmart.web.Login;
import com.boorisoogeo.EatSmart.web.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void update(Long memberId, MemberUpdateDto updateParam) {
        memberRepository.update(memberId, updateParam);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public boolean CheckDuplicateLoginId(String id) {
        Optional<Member> byLoginId = memberRepository.findByLoginId(id);

        if (byLoginId.isPresent()) {
            return true; // 중복됨
        }
        return false;//중복 안됨
    }
}
