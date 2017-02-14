package ru.sbrf.docedit.model.pagination.impl;

import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.pagination.Pagination;

import java.util.List;

/**
 * Simple implementation of {@code Page} interface.
 */
public class PageImpl<T> implements Page<T> {
    private final Pagination pagination;
    private final List<T> items;

    public PageImpl(Pagination pagination, List<T> items) {
        this.pagination = pagination;
        this.items = items;
    }

    @Override
    public List<T> getItems() {
        return items;
    }

    @Override
    public int getPageCount() {
        return pagination.getPageCount();
    }

    @Override
    public int getPageSize() {
        return pagination.getPageSize();
    }

    @Override
    public int getPageNo() {
        return pagination.getPageNo();
    }

    @Override
    public int getOffset() {
        return pagination.getOffset();
    }

    @Override
    public int getTotalItems() {
        return pagination.getTotalItems();
    }

    @Override
    public int getItemsCount() {
        return pagination.getItemsCount();
    }

    @Override
    public Order getSelectionOrder() {
        return pagination.getSelectionOrder();
    }
}
