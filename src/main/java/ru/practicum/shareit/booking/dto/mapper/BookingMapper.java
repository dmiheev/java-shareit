package ru.practicum.shareit.booking.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLiteDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.item.dto.mapper.ItemMapper.*;
import static ru.practicum.shareit.user.dto.mapper.UserMapper.*;

@UtilityClass
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
        if (booking.getItem() != null) {
            ItemDto itemDto = toItemDto(booking.getItem());
            bookingDto.setItem(itemDto);
            bookingDto.setItemId(itemDto.getId());
        }
        if (booking.getBooker() != null) {
            UserDto bookerDto = toUserDto(booking.getBooker());
            bookingDto.setBooker(bookerDto);
        }
        return bookingDto;
    }

    public Booking toBookingDb(BookingDto bookingDto, Item item, User booker) {
        return Booking.builder()
                .id(bookingDto.getId() != null ? bookingDto.getId() : 0L)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .item(item)
                .booker(booker)
                .build();
    }

    public Booking toBookingUpdate(BookingDto bookingDto, Booking booking) {
        Booking bookingUpdate = Booking.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(bookingDto.getStatus() != null ? bookingDto.getStatus() : booking.getStatus())
                .build();

        if (bookingDto.getItem() != null) {
            Item item = toItem(bookingDto.getItem());
            bookingUpdate.setItem(item);
        }
        if (bookingDto.getBooker() != null) {
            User booker = toUser(bookingDto.getBooker());
            bookingUpdate.setBooker(booker);
        }
        return bookingUpdate;
    }

    public BookingLiteDto toBookingLiteDto(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        BookingLiteDto bookingLiteDto = BookingLiteDto.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
        if (bookingDto.getItem() != null) {
            ItemDto item = bookingDto.getItem();
            bookingLiteDto.setItem(item);
        }
        if (bookingDto.getBooker() != null) {
            User booker = toUser(bookingDto.getBooker());
            bookingLiteDto.setBookerId(booker.getId());
        }
        return bookingLiteDto;
    }
}