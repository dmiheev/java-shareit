package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmptyFieldException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.GatewayHeaderException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.dto.mapper.ItemMapper.toItemDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        checkingUserId(userId);
        if (itemDto.getAvailable() == null || itemDto.getDescription() == null || itemDto.getName() == null) {
            throw new EmptyFieldException("Null fields in ItemDto element!");
        }
        if (itemDto.getName().isEmpty() || itemDto.getDescription().isEmpty()) {
            throw new EmptyFieldException("Empty fields in ItemDto element!");
        }
        log.debug("Creating item element : {}; for user {}", itemDto, userId);
        return toItemDto(itemRepository.create(toItem(itemDto), userId));
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        checkingUserId(userId);
        checkingItemId(itemDto.getId(), userId);
        log.debug("Updating item element: {}; for user {}", itemDto, userId);

        Item item = itemRepository.getItemById(itemDto.getId());
        item.setAvailable(itemDto.getAvailable() == null ? item.getAvailable() : itemDto.getAvailable());
        item.setDescription(itemDto.getDescription() == null || itemDto.getDescription().isBlank() ? item.getDescription() : itemDto.getDescription());
        item.setName(itemDto.getName() == null || itemDto.getName().isBlank() ? item.getName() : itemDto.getName());

        return toItemDto(itemRepository.update(item, userId));
    }

    @Override
    public ItemDto getItemById(long itemId, long userId) {
        checkingUserId(userId);
        log.debug("Getting item element by id: {}; for user {}", itemId, userId);
        return toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId) {
        checkingUserId(userId);
        log.debug("Getting items by user Id : {} ", userId);
        return itemRepository.getItemsByUserId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> getItemsBySearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        log.debug("Getting items by search : {} ", text);
        return itemRepository.getItemsBySearch(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkingUserId(long userId) {
        if (userId == -1) {
            throw new GatewayHeaderException("There is no user with header-Id : " + userId);
        }
        if (userService.getById(userId) == null) {
            throw new EntityNotFoundException("There is no user with Id : " + userId);
        }
    }

    private void checkingItemId(long itemId, long userId) {
        if (itemRepository.getItemById(itemId) == null) {
            throw new EntityNotFoundException("There is no Item with Id: " + itemId);
        }
        if (itemRepository.getItemsByUserId(userId) == null || !itemRepository.getItemsByUserId(userId).contains(itemRepository.getItemById(itemId))) {
            throw new EntityNotFoundException("The user with Id = " + userId + " does not own the item with Id = " + itemId);
        }
    }
}