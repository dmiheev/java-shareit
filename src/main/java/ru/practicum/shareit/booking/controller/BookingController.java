package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Creating a booking : {}", bookingDto);
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @RequestParam String approved,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Make approve status to booking: {}, status: {}", bookingId, approved);
        return bookingService.approveBooking(bookingId, userId, approved);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsForUser(@RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(defaultValue = "0")
                                                  @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10")
                                                  @Positive Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting info by user bookings");
        Pageable page = PageRequest.of(from / size, size);
        return bookingService.getAllBookingsByUserId(userId, state, page);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(defaultValue = "0")
                                                   @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10")
                                                   @Positive Integer size,
                                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting info by owner bookings");
        Pageable page = PageRequest.of(from / size, size);
        return bookingService.getAllBookingsByOwnerId(userId, state, page);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getInfoForBooking(@PathVariable Long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting info for booking: {}", bookingId);
        return bookingService.getBookingInfo(bookingId, userId);
    }
}