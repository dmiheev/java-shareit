package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.mapper.BookingMapper.*;
import static ru.practicum.shareit.user.dto.mapper.UserMapper.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto addBooking(BookingDto bookingDto, Long bookerId) {
        UserDto userFromDb = checkingUserId(bookerId);
        Item itemFromDb = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("There is no Item with id " + bookingDto.getItemId()));

        if (Objects.equals(itemFromDb.getOwner().getId(), bookerId)) {
            throw new EntityNotFoundException("Owner can't book his item");
        }
        if (!itemFromDb.getAvailable()) {
            throw new IncorrectDataException("Booking: Item is unavailable");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new IncorrectDataException("Booking: Dates are null!");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getStart().isEqual(bookingDto.getEnd())
                || bookingDto.getEnd().isBefore(LocalDateTime.now()) || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new IncorrectDataException("Booking: Problem in dates");
        }
        bookingDto.setStatus(BookingStatus.WAITING);
        return toBookingDto(bookingRepository.save(toBookingDb(bookingDto, itemFromDb, toUser(userFromDb))));
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, String approve) {
        checkingUserId(ownerId);
        BookingDto bookingDto = toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("There is no Booking with Id: " + bookingId)));

        if (!Objects.equals(bookingDto.getItem().getOwnerId(), ownerId)) {
            throw new EntityNotFoundException("User with id = " + ownerId + " is not an owner!");
        }

        switch (approve.toLowerCase()) {
            case "true": {
                if (bookingDto.getStatus().equals(BookingStatus.APPROVED)) {
                    throw new IncorrectDataException("Status is Approved");
                }
                bookingDto.setStatus(BookingStatus.APPROVED);
                break;
            }
            case "false": {
                bookingDto.setStatus(BookingStatus.REJECTED);
                break;
            }
            default:
                throw new IncorrectDataException("Incorrect data in approve method");
        }
        Booking bookingToUpdate = toBookingUpdate(bookingDto, bookingRepository.findById(bookingId).get());
        bookingRepository.save(bookingToUpdate);
        return toBookingDto(bookingToUpdate);
    }

    @Override
    public BookingDto getBookingInfo(Long bookingId, Long userId) {
        checkingUserId(userId);
        BookingDto bookingDto = toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("There is no Booking with Id: " + bookingId)));
        if (!Objects.equals(bookingDto.getItem().getOwnerId(), userId) && !Objects.equals(bookingDto.getBooker().getId(), userId)) {
            throw new EntityNotFoundException("User with id = " + userId + " is not an owner!");
        }
        return bookingDto;
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId, String state) {
        checkingUserId(userId);
        checkingBookingState(state);
        List<Booking> bookings;

        BookingState bookingState = BookingState.getEnumValue(state.toUpperCase());

        switch (bookingState) {
            case WAITING: {
                bookings = bookingRepository.findAllByBookerIdAndWaitingStatus(userId, BookingStatus.WAITING, SORT_BY_START_DESC);
                break;
            }
            case REJECTED: {
                bookings = bookingRepository.findAllByBookerIdAndRejectedStatus(userId, List.of(BookingStatus.REJECTED, BookingStatus.CANCELED), SORT_BY_START_DESC);
                break;
            }
            case CURRENT: {
                bookings = bookingRepository.findAllByBookerIdAndCurrentStatus(userId, LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            }
            case FUTURE: {
                bookings = bookingRepository.findAllByBookerIdAndFutureStatus(userId, LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            }
            case PAST: {
                bookings = bookingRepository.findAllByBookerIdAndPastStatus(userId, LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            }
            case ALL: {
                bookings = bookingRepository.findAllByBooker_Id(userId, SORT_BY_START_DESC);
                break;
            }
            default:
                bookings = new ArrayList<>();
        }
        return new ArrayList<>(bookings).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state) {
        checkingUserId(ownerId);
        checkingBookingState(state);
        List<Long> userItemsIds = itemRepository.findByOwner_Id(ownerId, Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        if (userItemsIds.isEmpty()) {
            throw new IncorrectDataException("This method only for users who have >1 items");
        }
        List<Booking> bookings;

        BookingState bookingState = BookingState.getEnumValue(state.toUpperCase());

        switch (bookingState) {
            case WAITING: {
                bookings = bookingRepository.findAllByOwnerItemsAndWaitingStatus(userItemsIds, BookingStatus.WAITING, SORT_BY_START_DESC);
                break;
            }
            case REJECTED: {
                bookings = bookingRepository.findAllByOwnerItemsAndRejectedStatus(userItemsIds, List.of(BookingStatus.REJECTED, BookingStatus.CANCELED), SORT_BY_START_DESC);
                break;
            }
            case CURRENT: {
                bookings = bookingRepository.findAllByOwnerItemsAndCurrentStatus(userItemsIds, LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            }
            case FUTURE: {
                bookings = bookingRepository.findAllByOwnerItemsAndFutureStatus(userItemsIds, LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            }
            case PAST: {
                bookings = bookingRepository.findAllByOwnerItemsAndPastStatus(userItemsIds, LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            }
            case ALL: {
                bookings = bookingRepository.findAllByOwnerItems(userItemsIds, SORT_BY_START_DESC);
                break;
            }
            default:
                bookings = new ArrayList<>();
        }
        return new ArrayList<>(bookings).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    private UserDto checkingUserId(Long userId) {
        if (userId == -1) {
            throw new IncorrectDataException("There is no user with header-Id : " + userId);
        }
        return toUserDto(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("There is no user with id: " + userId)));
    }

    private void checkingBookingState(String state) {
        try {
            BookingState.valueOf(state);
        } catch (Exception e) {
            throw new UnsupportedStatusException(state);
        }
    }

}