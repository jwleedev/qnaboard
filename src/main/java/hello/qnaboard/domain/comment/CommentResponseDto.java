package hello.qnaboard.domain.comment;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {

    private Long commentId;
    private Long postId;
    private Long memberId;
    private String content;
    private String name;
}
