package ru.sbrf.docedit.model.pagination.impl;

import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Pagination;

/**
 * Simple implementation of page.
 * <p>
 * Note that first element index is 0.
 */
public class PaginationImpl implements Pagination {
    private final int total;
    private final int offset;
    private final int pageSize;
    private final int pageCount;
    private final int pageNo;
    private final int itemCount;
    private final Order order;

    public PaginationImpl(int pageNo, int pageSize, int total) {
        this(pageNo, pageSize, total, Order.ASC);
    }

    public PaginationImpl(int pageNo, int pageSize, int total, Order order) {
        this.pageNo = pageNo < 0 ? 0 : pageNo;
        this.pageSize = pageSize <= 0 ? 1 : pageSize;
        this.total = total;

        final int pageCount = total / this.pageSize;
        this.pageCount = total > (pageCount * this.pageSize) ? pageCount + 1 : pageCount;
        this.offset = pageNo * pageSize;

        int itemCount = this.total - this.offset;
        itemCount = itemCount >= this.pageSize ? this.pageSize : itemCount;
        this.itemCount = itemCount < 0 ? 0 : itemCount;

        this.order = order;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getPageNo() {
        return pageNo;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getTotalItems() {
        return total;
    }

    @Override
    public int getItemsCount() {
        return itemCount;
    }

    @Override
    public Order getSelectionOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "PaginationImpl{" +
                "total=" + total +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", pageNo=" + pageNo +
                ", itemCount=" + itemCount +
                '}';
    }
}
