package hello.qnaboard.repository.member.mybatis;

import hello.qnaboard.domain.member.Member;
import hello.qnaboard.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MyBatisMemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        //given
        Member member = new Member("widkk@naver.com", "kiki", "kk239999");

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(member.getMemberId()).get();
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void findById() {
        //given
        Member member = new Member("widkk@naver.com", "kiki", "kk239999");
        //when
        Member savedMember = memberRepository.save(member);

        //then
        Optional<Member> findMember = memberRepository.findById(member.getMemberId());
        assertThat(findMember).isPresent().contains(savedMember);
    }

    @Test
    void findByLoginId() {
        Member member = new Member("widkk@naver.com", "kiki", "kk239999");
        Member savedMember = memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findByLoginId(member.getEmail());
        assertThat(findMember).isPresent().contains(savedMember);
    }

    @Test
    void findAll() {
        Member member1 = new Member("widkk@naver.com", "kiki1", "kk239999");
        Member member2 = new Member("widk@naver.com", "kiki2", "kk239998");
        Member member3 = new Member("wid@naver.com", "kiki3", "kk239988");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> members = memberRepository.findAll();
        assertThat(members).contains(member1, member2, member3);
    }
}