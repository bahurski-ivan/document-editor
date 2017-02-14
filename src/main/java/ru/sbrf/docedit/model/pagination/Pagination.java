package ru.sbrf.docedit.model.pagination;

/**
 * Interface that represents pagination request.
 */
public interface Pagination {
    /**
     * Returns total page count.
     */
    int getPageCount();

    /**
     * Returns amount of elements per page.
     */
    int getPageSize();

    /**
     * Returns current page number.
     */
    int getPageNo();


    /**
     * Returns offset to the first element in this page.
     */
    int getOffset();

    /**
     * Returns total amount of items.
     */
    int getTotalItems();

    /**
     * Returns item count inside this page.
     */
    int getItemsCount();

    /**
     * Returns order of selection.
     */
    Order getSelectionOrder();
}
