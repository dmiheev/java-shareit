package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@DataJpaTest
@Sql(value = {"/set-up-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/set-up-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookingRepositoryIntegrationTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    private long userId;
    private long ownerId;
    private PageRequest defaultPageRequest;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        ownerId = 1L;
        userId = 2L;
        defaultPageRequest = PageRequest.of(0, 10);
        now = LocalDateTime.now();
    }

    @Test
    public void findAllByUserUserIdOrderByEndTimeDescWhenFoundTwoBookingsReturnFourBookings() {
        List<Booking> bookings = bookingRepository.findAllByBooker_Id(userId, defaultPageRequest);

        assertEquals(4, bookings.size());
        assertEquals(4L, bookings.get(3).getId());
        assertEquals(3L, bookings.get(2).getId());
        assertEquals(2L, bookings.get(1).getId());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    public void findAllByUserUserIdOrderByEndTimeDescWhenBookingsNotFoundReturnEmptyList() {
        long userIdWithoutBookings = 1L;

        List<Booking> bookings = bookingRepository.findAllByBooker_Id(userIdWithoutBookings, defaultPageRequest);

        assertEquals(0, bookings.size());
    }

    @Test
    public void findAllBookingByOwnerIdWhenInvokedMethodReturnFourBookings() {
        List<Booking> bookings = bookingRepository.findAllByItem_Owner_Id(ownerId);

        assertEquals(4, bookings.size());
    }

    @Test
    public void findAllBookingByOwnerIdWhenOwnerDoesntHaveBookingsReturnEmptyList() {
        long ownerIdWithoutBookings = 2L;

        List<Booking> bookings = bookingRepository.findAllByItem_Owner_Id(ownerIdWithoutBookings);

        assertEquals(0, bookings.size());
    }
}