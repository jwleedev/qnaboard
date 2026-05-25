package hello.qnaboard.domain.login;

import hello.qnaboard.domain.member.Member;
import hello.qnaboard.domain.member.MemberRepository;
import hello.qnaboard.domain.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    LoginService loginService;

    @Test
    void login() {
        Member member = new Member("jjd@naver.com", "jjd", "testgood!");
        Member saveMember = memberService.save(member);

        Member loginMember = loginService.login("jjd@naver.com", "testgood!");

        assertThat(loginMember).isNotNull();
        assertThat(loginMember.getEmail()).isEqualTo(member.getEmail());
    }
}