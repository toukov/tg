package com.example.tg;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Utils {
    private static final int RETAIN = 1;
    private static final int REJECT = 0;

    public static Long getUnixTimestampMillisDaysAgo(int days) {
        return Instant.now().minus(Duration.ofDays(days)).toEpochMilli();
    }

    /**
     * In pagination, calculates how many records should the current page have at maximum given the
     * maximum number of records in all the pages (count)
     * @param page page number
     * @param pageSize page size
     * @param count how many records in all the pages together
     * @return how many records in current page
     */
    public static int getLimitWithCount(int page, int pageSize, int count) {
        int lastIndexOnPage = ((page + 1) * pageSize) - 1;
        int firstIndexOnPage = page * pageSize;
        int lastIndexOfLastPage = count - 1;

        if (lastIndexOnPage <= lastIndexOfLastPage) {
            return pageSize;
        } else if (firstIndexOnPage <= lastIndexOfLastPage) {
            return count - page * pageSize;
        }
        return 0;
    }

    /**
     * In pagination, calculates how many records to skip from the beginning in order to reach the
     * current page.
     * @param page page number
     * @param pageSize page size
     * @return how many records to skip
     */
    public static int getSkip(int page, int pageSize) {
        return page * pageSize;
    }

    /**
     * Validate pagination parameters.
     *
     * @param page page number
     * @param pageSize page size
     * @return do the parameters form valid data to do pagination with
     */
    public static boolean validatePagination(Integer page, Integer pageSize) {
        if (page != null || pageSize != null) {
            if (page == null
                    || pageSize == null
                    || page < 0
                    || pageSize < 1) {
                throw ResponseFactory.generateBadRequestException(
                        "page must be greater than or equal to 0 and page_size"
                                + " must be greater than 0");
            }
            return true;
        }
        return false;
    }

    /**
     * Checks that all the fields in filters are one or all the fields are zero.
     * @param filters filters to check
     * @return whether to include (true) the fields in filters or to reject them
     */
    public static boolean validateFiltersAndGetRetain(Map<String, Integer> filters) {
        if (filters.isEmpty()) {
            return false;
        }

        int keepOrNot = filters.entrySet().stream().findAny().get().getValue();

        filters.entrySet().stream().forEach(entry -> {
            if (entry.getValue() != keepOrNot
                    || (entry.getValue() != RETAIN && entry.getValue() != REJECT)) {
                throw ResponseFactory.generateBadRequestException(
                        "All filter fields must have the same value and that value must be one of "
                                + RETAIN + " or " + REJECT);
            }
        });

        return keepOrNot == RETAIN;
    }
}
