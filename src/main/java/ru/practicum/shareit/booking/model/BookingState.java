package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.UnsupportedStatusException;

import java.util.Arrays;

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

    public static String checkState(String state) {
        return Arrays.stream(BookingState.values())
                .map(BookingState::name)
                .filter(x -> x.equals(state))
                .findFirst()
                .orElse("");
    }
}