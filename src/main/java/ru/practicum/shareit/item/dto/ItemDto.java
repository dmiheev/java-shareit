package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    /**
     * TODO
     * import ru.practicum.shareit.request.model.ItemRequest;
     * <p>
     * private List<ItemRequest> requests;
     * private BookingLiteDto nextBooking;
     * private BookingLiteDto lastBooking;
     */
    private Long ownerId;
    private List<CommentDto> comments;
}