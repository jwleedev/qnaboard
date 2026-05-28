package hello.qnaboard.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long commentId;
    private Long postId;
    private Long memberId;
    private String content;
    private LocalDateTime createdAt;
}
