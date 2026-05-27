package hello.qnaboard.domain.post;

import hello.qnaboard.domain.member.Member;
import hello.qnaboard.domain.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    MemberService memberService;

    private Post createAndSavePost() {
        Member member = new Member("jjd@naver.com", "jjd", "testgood!");
        memberService.save(member);

        Post post = new Post(member.getMemberId(), "테스트 제목입니다.", "테스트 내용입니다.");
        return postService.save(post);
    }

    @Test
    void save() {
        Post savedPost = createAndSavePost();

        assertThat(savedPost.getPostId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("테스트 제목입니다.");
        assertThat(savedPost.getContent()).isEqualTo("테스트 내용입니다.");
    }

    @Test
    void findById() {
        Post savedPost = createAndSavePost();

        PostDetailDto findPost = postService.findById(savedPost.getPostId()).get();

        Assertions.assertThat(findPost.getTitle()).isEqualTo("테스트 제목입니다.");
        Assertions.assertThat(findPost.getContent()).isEqualTo("테스트 내용입니다.");
    }

    @Test
    void updateViewCount() {
        Post savedPost = createAndSavePost();

        postService.updateViewCount(savedPost.getPostId());

        PostDetailDto findPost = postService.findById(savedPost.getPostId()).get();
        Assertions.assertThat(findPost.getViewCount()).isEqualTo(1);
    }

    @Test
    void updatePost() {
        Post savedPost = createAndSavePost();
        Long postId = savedPost.getPostId();

        PostUpdateDto updateDto = new PostUpdateDto();
        updateDto.setPostId(postId);
        updateDto.setTitle("수정된 제목");
        updateDto.setContent("수정된 내용");

        postService.updatePost(updateDto);

        PostDetailDto findPost = postService.findById(postId).get();
        Assertions.assertThat(findPost.getTitle()).isEqualTo("수정된 제목");
        Assertions.assertThat(findPost.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    void delete() {
        Post savedPost = createAndSavePost();

        postService.delete(savedPost.getPostId());

        Optional<PostDetailDto> findPost = postService.findById(savedPost.getPostId());
        Assertions.assertThat(findPost).isEmpty();
    }
}