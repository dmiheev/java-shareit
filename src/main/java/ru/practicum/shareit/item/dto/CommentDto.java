package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String authorName;
    private ItemDto item;
    private String text;
    private LocalDateTime created;
}