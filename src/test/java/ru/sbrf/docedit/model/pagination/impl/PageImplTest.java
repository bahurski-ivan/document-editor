package ru.sbrf.docedit.model.pagination.impl;

import org.junit.Test;
import ru.sbrf.docedit.model.pagination.Pagination;

import static org.junit.Assert.assertEquals;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
public class PageImplTest {
    @Test
    public void testMidRange() throws Exception {
        Pagination p = new PaginationImpl(10, 5, 100);

        // offset = 50
        // itemCount = 5
        // pageCount = 20

        assertEquals(p.getOffset(), 50);
        assertEquals(p.getPageCount(), 20);
        assertEquals(p.getPageNo(), 10);
        assertEquals(p.getTotalItems(), 100);
        assertEquals(p.getPageSize(), 5);
        assertEquals(p.getItemsCount(), 5);
    }

    @Test
    public void testEndRange() throws Exception {
        Pagination p = new PaginationImpl(20, 5, 103);

        // offset = 100
        // itemCount = 3
        // pageCount = 21

        assertEquals(p.getOffset(), 100);
        assertEquals(p.getPageCount(), 21);
        assertEquals(p.getPageNo(), 20);
        assertEquals(p.getTotalItems(), 103);
        assertEquals(p.getPageSize(), 5);
        assertEquals(p.getItemsCount(), 3);
    }

    @Test
    public void testOutOfRange() throws Exception {
        Pagination p = new PaginationImpl(22, 5, 103);

        // offset = 110
        // itemCount = 3
        // pageCount = 21

        assertEquals(p.getOffset(), 110);
        assertEquals(p.getPageCount(), 21);
        assertEquals(p.getPageNo(), 22);
        assertEquals(p.getTotalItems(), 103);
        assertEquals(p.getPageSize(), 5);
        assertEquals(p.getItemsCount(), 0);
    }

    @Test
    public void testBadInput() throws Exception {
        Pagination p = new PaginationImpl(-100, 0, 103);

        // offset = 0
        // itemCount = 1
        // pageCount = 21

        assertEquals(p.getOffset(), 0);
        assertEquals(p.getPageCount(), 103);
        assertEquals(p.getPageNo(), 0);
        assertEquals(p.getTotalItems(), 103);
        assertEquals(p.getPageSize(), 1);
        assertEquals(p.getItemsCount(), 1);
    }
}