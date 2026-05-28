package hello.qnaboard.web.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentSaveForm {

    private Long postId;

    @NotBlank
    private String content;
}
