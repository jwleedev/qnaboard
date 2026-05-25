package hello.qnaboard.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository{

    private final MemberMapper memberMapper;

    @Override
    public Member save(Member member) {
        memberMapper.save(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberMapper.findById(memberId);
    }

    @Override
    public Optional<Member> findByLoginId(String email) {
        return memberMapper.findByLoginId(email);
    }

    @Override
    public List<Member> findAll() {
        return memberMapper.findAll();
    }
}
