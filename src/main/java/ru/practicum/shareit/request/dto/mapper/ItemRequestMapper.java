package ru.practicum.shareit.request.dto.mapper;


import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.user.dto.mapper.UserMapper.toUserDto;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requester) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .creationDate(itemRequestDto.getCreated())
                .requester(requester)
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<Item> items) {

        Collection<RequestItemDto> requestItem = new ArrayList<>();

        items.forEach(item -> {
            requestItem.add(toItemRequest(item));
        });

        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreationDate())
                .items(requestItem)
                .build();

        if (itemRequest.getRequester() != null) {
            UserDto requester = toUserDto(itemRequest.getRequester());
            requestDto.setRequester(requester);
        }

        return requestDto;
    }

    public static RequestItemDto makeResultItemDto(Item item) {
        return RequestItemDto.builder()
                .name(item.getName())
                .ownerId(item.ownerId())
                .id(item.getId())
                .requestId(item.getRequest().getId())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static RequestItemDto toItemRequest(Item item) {
        return RequestItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwner().getId())
                .available(item.getAvailable())
                .requestId(item.getRequest().getId())
                .build();
    }

    public static List<ItemRequestDto> toItemRequestListDto(List<ItemRequest> requests,
                                                            Map<Long, List<Item>> mapItems) {
        List<ItemRequestDto> ir = new ArrayList<>();

        requests.forEach(req -> {
            List<Item> items = mapItems.get(req.getId());
            if (items == null) {
                items = new ArrayList<>();
            }
            Collection<RequestItemDto> requestItem = new ArrayList<>();
            items.forEach(item -> requestItem.add(toItemRequest(item)));

            ItemRequestDto requestDto = ItemRequestDto.builder()
                    .id(req.getId())
                    .description(req.getDescription())
                    .created(req.getCreationDate())
                    .items(requestItem)
                    .build();
            ir.add(requestDto);
        });

        return ir;
    }
}
