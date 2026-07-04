package com.fudo.shared.pagination;

public class PagedRequest {

    private int page = 1;
    private int pageSize = 20;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page < 1 ? 1 : page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize > 100 ? 100 : Math.max(pageSize, 1);
    }

    public int getSkip() {
        return (page - 1) * pageSize;
    }
}
