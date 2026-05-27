package hello.qnaboard.domain.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {

    private Long postId;
    private Long memberId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post(Long memberId, String title, String content) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
    }
}
