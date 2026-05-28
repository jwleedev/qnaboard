package hello.qnaboard.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public void save(Comment comment) {
        commentMapper.save(comment);
    }

    public Optional<Comment> findById(Long commentId) {
        return commentMapper.findById(commentId);
    }

    public void delete(Long commentId) {
        commentMapper.delete(commentId);
    }

    public List<CommentResponseDto> findAllByPostId(Long postId) {
        return commentMapper.findAllByPostId(postId);
    }
}
