package hello.qnaboard.domain.member;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    void save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByLoginId(String email);

    List<Member> findAll();
}
