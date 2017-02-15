package ru.sbrf.docedit.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sbrf.docedit.model.pagination.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class PageResultDto<T> {
    @JsonProperty("item_count")
    private int itemCount;
    @JsonProperty("page_no")
    private int pageNo;
    @JsonProperty("page_total")
    private int pageTotal;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("offset")
    private int offset;
    @JsonProperty("items")
    private List<T> items;

    public PageResultDto() {
    }

    public PageResultDto(int itemCount, int pageNo, int pageTotal, int pageSize, int offset, List<T> items) {
        this.itemCount = itemCount;
        this.pageNo = pageNo;
        this.pageTotal = pageTotal;
        this.pageSize = pageSize;
        this.offset = offset;
        this.items = items;
    }


    public static <T, R> PageResultDto<R> toDto(Page<T> page, Function<T, R> itemConverter) {
        return new PageResultDto<>(
                page.getItemsCount(),
                page.getPageNo(),
                page.getTotalItems(),
                page.getPageSize(),
                page.getOffset(),
                page.getItems().stream()
                        .map(itemConverter)
                        .collect(Collectors.toList())
        );
    }


    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "PageResultDto{" +
                "itemCount=" + itemCount +
                ", pageNo=" + pageNo +
                ", pageTotal=" + pageTotal +
                ", pageSize=" + pageSize +
                ", offset=" + offset +
                ", items=" + items +
                '}';
    }
}
