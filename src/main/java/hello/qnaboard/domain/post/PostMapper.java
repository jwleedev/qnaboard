package hello.qnaboard.domain.post;

import hello.qnaboard.domain.common.PageRequest;
import hello.qnaboard.web.post.PostUpdateForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    void save(Post post);

    Optional<PostDetailDto> findById(Long postId);

    void updateViewCount(Long postId);

    void updatePost(PostUpdateDto updateDto);

    void delete(Long postId);

    List<PostDetailDto> findAll(PageRequest pageRequest);

    int countAll();
}
