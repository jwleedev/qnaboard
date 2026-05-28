package hello.qnaboard.domain.common;

import lombok.Getter;

@Getter
public class Pagination {

    private int totalPostCount;
    private int totalPageCount;
    private int startPage;
    private int endPage;
    private boolean existPrevPage; //이전 묶음(<) 페이지 존재 여부
    private boolean existNextPage; //다음 묶음(>) 페이지 존재 여부

    public Pagination(int totalPostCount, PageRequest pageRequest) {
        if (totalPostCount > 0) {
            this.totalPostCount = totalPostCount;
            calculation(pageRequest);
        }
    }

    private void calculation(PageRequest pageRequest) {
        // 하단에 보여줄 페이지 버튼의 개수
        int displayPageNum = 2;

        // 전체 페이지 수 계산
        this.totalPageCount = (int) Math.ceil(this.totalPostCount / (double) pageRequest.getSize());

        // 현재 화면의 시작 페이지, 끝 페이지 계산
        this.endPage = (int) (Math.ceil(pageRequest.getPage() / (double) displayPageNum) * displayPageNum);
        this.startPage = (this.endPage - displayPageNum) + 1;

        // 끝 페이지 보정 (예: 총 7페이지인데 endPageRK 10으로 계산된 경우 7로 변경)
        if (this.endPage > this.totalPageCount) {
            this.endPage = this.totalPageCount;
        }

        // 이전, 다음 버튼 존재 여부
        this.existPrevPage = this.startPage != 1;
        this.existNextPage = (this.endPage * pageRequest.getSize()) < this.totalPostCount;
    }
}
