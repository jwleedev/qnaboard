package hello.qnaboard.domain.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDetailDto {

    private Long postId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;

    private Long memberId;
    private String name;
}

