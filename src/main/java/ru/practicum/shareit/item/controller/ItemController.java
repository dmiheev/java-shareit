package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Creating item element {}", itemDto);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Updating item element by id {}", itemId);
        itemDto.setId(itemId);
        return itemService.update(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting item by id: {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping()
    public Collection<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting all items by userId {}", userId);
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearch(@RequestParam String text) {
        log.info("Getting items by search text: {}", text);
        return itemService.getItemsBySearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createCommentToItem(@PathVariable Long itemId, @RequestBody CommentDto comment, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Creating comment to item by userId {}", userId);
        comment.setCreated(LocalDateTime.now());
        return itemService.addCommentToItem(userId, itemId, comment);
    }
}