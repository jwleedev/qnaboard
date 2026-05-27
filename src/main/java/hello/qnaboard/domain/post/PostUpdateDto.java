package hello.qnaboard.domain.post;

import lombok.Data;

@Data
public class PostUpdateDto {
    private Long postId;
    private String title;
    private String content;
}
