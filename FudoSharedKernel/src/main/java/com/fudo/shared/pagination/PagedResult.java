package com.fudo.shared.pagination;

import java.util.List;

public class PagedResult<T> {

    private final List<T> items;
    private final int totalCount;
    private final int page;
    private final int pageSize;

    public PagedResult(List<T> items, int totalCount, int page, int pageSize) {
        this.items = items;
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return pageSize == 0 ? 0 : (int) Math.ceil(totalCount / (double) pageSize);
    }

    public boolean isHasNextPage() {
        return page < getTotalPages();
    }

    public boolean isHasPreviousPage() {
        return page > 1;
    }
}
