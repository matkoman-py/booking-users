package com.bookingusers.exception;

public class ActiveBookingsException extends RuntimeException {
    public ActiveBookingsException(String message) {
        super(message);
    }
}
