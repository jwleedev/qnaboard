package hello.qnaboard.domain.comment;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    void save(Comment comment);

    void delete(Long commentId);

    Optional<Comment> findById(Long commentId);

    List<CommentResponseDto> findAllByPostId(Long postId);
}
