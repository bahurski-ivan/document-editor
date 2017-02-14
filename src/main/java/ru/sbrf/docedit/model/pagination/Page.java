package ru.sbrf.docedit.model.pagination;

import java.util.List;

/**
 * Represents page result.
 */
public interface Page<T> extends Pagination {
    /**
     * Returns list of items inside page.
     */
    List<T> getItems();
}
