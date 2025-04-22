package com.ss.order.constant;

public final class Constants {
    private Constants() {
    }

    // For Order status update
    public static final int SAME_ORDER_STATUS = 1;
    public static final int SUCCESS = 0;
    public static final int SUCCESS_BUT_SEND_EMAIL_FAILED = -1;
    public static final int ORDER_NOT_FOUND = -2;
    public static final int ORDER_STATUS_UPDATE_FAILED = -3;

    // For email purpose and subject
    public static final int ORDER_CONFIRMED = 1;
    public static final int ORDER_UPDATED = 2;
}