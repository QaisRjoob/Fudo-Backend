package com.fudo.shared.pagination;

import java.util.List;

public class CursorPage<T> {

    private final List<T> items;
    private final String nextCursor;

    public CursorPage(List<T> items, String nextCursor) {
        this.items = items;
        this.nextCursor = nextCursor;
    }

    public List<T> getItems() {
        return items;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public boolean isHasMore() {
        return nextCursor != null;
    }
}
