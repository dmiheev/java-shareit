package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@DataJpaTest
@Sql(value = {"/set-up-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/set-up-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ItemRepositoryIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    private long userId;
    private int amountItems;
    private PageRequest defaultPageRequest;

    @BeforeEach
    public void setUp() {
        defaultPageRequest = PageRequest.of(0, 10);
        userId = 1L;
        amountItems = 3;
    }

    @Test
    public void findAllByUserIdWhenPageRequestDefaultReturnThreeItems() {
        assertEquals(amountItems, itemRepository.findByOwner_Id(userId, defaultPageRequest).size());
    }

    @Test
    public void findAllByUserIdWhenPageRequestSizeIsTwoReturnTwoItems() {
        assertEquals(2, itemRepository.findByOwner_Id(userId, PageRequest.of(0, 2)).size());
    }

    @Test
    public void findAllByUserIdWhenUserNotFoundReturnEmptyList() {
        long unknownUser = 100L;

        assertEquals(0, itemRepository.findByOwner_Id(unknownUser, defaultPageRequest).size());
    }

    @Test
    public void findAllByUserIdWhenUserDontHaveItemsReturnEmptyList() {
        long userWithoutItems = 2L;

        assertEquals(0, itemRepository.findByOwner_Id(userWithoutItems, defaultPageRequest).size());
    }

    @Test
    public void findAllByRequestIdWhenInvokedMethodReturnTwoItems() {
        assertEquals(2, itemRepository.findByRequest_Id(1L).size());
    }

    @Test
    public void findAllByRequestIdWhenItemsNotFoundReturnEmptyList() {
        long unknownRequestId = 100L;

        assertEquals(0, itemRepository.findByRequest_Id(unknownRequestId).size());
    }

    @Test
    public void findAllByRequestsWhenItemsNotFoundByListReturnEmptyList() {
        long unknownRequestId = 100L;

        List<Long> requests = List.of(unknownRequestId);

        assertEquals(0, itemRepository.findByRequest_IdIn(requests).size());
    }

    @Test
    public void findAllByRequestsWhenTwoItemsFoundReturnTwoItems() {
        long requestId = 1L;

        List<Long> requests = List.of(requestId);

        assertEquals(2, itemRepository.findByRequest_IdIn(requests).size());
    }
}