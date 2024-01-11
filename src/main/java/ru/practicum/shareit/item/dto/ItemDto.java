package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingLiteDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Builder
@Data
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<ItemRequestDto> requests;
    private BookingLiteDto nextBooking;
    private BookingLiteDto lastBooking;
    private Long ownerId;
    private List<CommentDto> comments;
}