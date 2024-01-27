package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/requests")
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addNewRequest(@RequestBody ItemRequestDto requestDto,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Creating item request element {}", requestDto);
        return itemRequestService.addNewRequest(requestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllUserItemsWithResponses(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Getting collection of users' items requests");
        return itemRequestService.getAllUserRequestsWithResponses(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllCreatedRequests(@RequestParam(defaultValue = "0")
                                                            @PositiveOrZero Integer from,
                                                            @RequestParam(defaultValue = "10")
                                                            @Positive Integer size,
                                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Getting collection of created requests");
        Pageable page = PageRequest.of(from, size, Sort.by("creationDate").descending());
        return itemRequestService.getAllRequestsToResponse(userId, page);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Long requestId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Getting request by id: {}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }
}
