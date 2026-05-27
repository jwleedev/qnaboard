package hello.qnaboard.domain.post;

import hello.qnaboard.web.post.PostUpdateForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface PostMapper {

    void save(Post post);

    Optional<PostDetailDto> findById(Long postId);

    void updateViewCount(Long postId);

    void updatePost(PostUpdateDto updateDto);

    void delete(Long postId);
}
