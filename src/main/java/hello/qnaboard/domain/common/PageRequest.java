package hello.qnaboard.domain.common;

import lombok.Data;

@Data
public class PageRequest {

    private int page;
    private int size;

    public PageRequest() {
        this.page = 1;
        this.size = 10;
    }

    public int getOffset() {
        return (this.page - 1) * this.size;
    }
}
