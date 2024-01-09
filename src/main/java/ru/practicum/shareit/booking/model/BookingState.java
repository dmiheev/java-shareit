package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.UnsupportedStatusException;

public enum BookingState {

    WAITING,
    REJECTED,
    CURRENT,
    FUTURE,
    PAST,
    ALL;

    public static BookingState getEnumValue(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (Exception e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }
}