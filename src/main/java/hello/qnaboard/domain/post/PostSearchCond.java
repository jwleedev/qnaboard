package hello.qnaboard.domain.post;

import lombok.Data;

@Data
public class PostSearchCond {

    private String type;
    private String keyword;

    public PostSearchCond() {
    }

    public PostSearchCond(String type, String keyword) {
        this.type = type;
        this.keyword = keyword;
    }
}
