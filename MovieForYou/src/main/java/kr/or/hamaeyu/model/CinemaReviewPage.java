package kr.or.hamaeyu.model;

import lombok.Getter;

@Getter
public class CinemaReviewPage {
    private final int page;
    private final int size;
    private final int total;
    private final int pages;
    private final int offset;

    public CinemaReviewPage(int page, int size, int total) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.pages = (int) Math.ceil(total / (double) size);
        this.offset = (page - 1) * size;
    }
}
