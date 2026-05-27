package hello.qnaboard.web.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostUpdateForm {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
