package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(value = {"/set-up-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/set-up-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ItemRequestRepositoryIntegrationTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private long userId;
    private long requestId;
    private long userWithoutRequests;

    @BeforeEach
    public void setUp() {
        userId = 2L;
        requestId = 1L;
        userWithoutRequests = 1L;
    }

    @Test
    public void getItemRequestsByUserWhenInvokedMethodReturnListItemRequests() {
        assertEquals(3, itemRequestRepository.findAllByRequester_Id(userId).size());
    }

    @Test
    public void getItemRequestsByUserWhenItemRequestsNotFoundReturnEmptyList() {
        assertEquals(0, itemRequestRepository.findAllByRequester_Id(userWithoutRequests).size());
    }

    @Test
    public void findAllByUserUserIdNotLikeWhenInvokedMethodReturnEmptyList() {
        assertEquals(0, itemRequestRepository.findAllByAllOtherUsers(userId, PageRequest.of(0,  10)).size());
    }

    @Test
    public void findAllByUserUserIdNotLikeWhenInvokedMethodReturnListWithThreeRequests() {
        assertEquals(3, itemRequestRepository.findAllByAllOtherUsers(userWithoutRequests, PageRequest.of(0,  10)).size());
    }
}