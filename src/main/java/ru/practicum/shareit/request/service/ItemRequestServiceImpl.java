package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validator.ItemRequestValidator;
import ru.practicum.shareit.validator.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.dto.mapper.ItemRequestMapper.toItemRequest;
import static ru.practicum.shareit.request.dto.mapper.ItemRequestMapper.toItemRequestDto;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserValidator userValidator;
    private final ItemRequestValidator itemRequestValidator;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addNewRequest(ItemRequestDto requestDto, Long userId) {
        User requester = userValidator.validateUserIdAndReturn(userId);
        requestDto.setCreated(LocalDateTime.now());
        itemRequestValidator.validateItemRequestData(requestDto);
        return toItemRequestDto(itemRequestRepository.save(toItemRequest(requestDto, requester)), new ArrayList<>());
    }

    @Override
    public Collection<ItemRequestDto> getAllUserRequestsWithResponses(Long userId) {
        userValidator.validateUserId(userId);

        List<ItemRequest> requests = itemRequestRepository.findAllByRequester_Id(userId);
        Map<Long, List<Item>> map = itemRepository.findAllItemsByRequestIds(requests.stream()
                .map(x -> x.getId()).collect(Collectors.toList()));

        return ItemRequestMapper.toItemRequestListDto(requests, map);
    }

    @Override
    public Collection<ItemRequestDto> getAllRequestsToResponse(Long userId, Pageable page) {
        userValidator.validateUserId(userId);

        List<ItemRequest> requests = itemRequestRepository.findAllByAllOtherUsers(userId, page);
        Map<Long, List<Item>> map = itemRepository.findAllItemsByRequestIds(requests.stream()
                .map(x -> x.getId()).collect(Collectors.toList()));

        return ItemRequestMapper.toItemRequestListDto(requests, map);
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        userValidator.validateUserId(userId);
        ItemRequest request = itemRequestValidator.validateItemRequestIdAndReturns(requestId);
        List<Item> items = itemRepository.findByRequest_Id(requestId);
        return toItemRequestDto(request, items);
    }
}
