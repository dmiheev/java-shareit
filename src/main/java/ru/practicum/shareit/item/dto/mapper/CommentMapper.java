package ru.practicum.shareit.item.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.item.dto.mapper.ItemMapper.*;
import static ru.practicum.shareit.user.dto.mapper.UserMapper.*;

@UtilityClass
public class CommentMapper {

    public Comment toCommentDb(CommentDto commentDto, User author, Item item) {
        return Comment.builder()
                .id(commentDto.getId() != null ? commentDto.getId() : 0L)
                .author(author)
                .item(item)
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
        if (comment.getItem() != null) {
            ItemDto itemDto = toItemDto(comment.getItem());
            commentDto.setItem(itemDto);
        }
        if (comment.getAuthor() != null) {
            UserDto userDto = toUserDto(comment.getAuthor());
            commentDto.setAuthorName(userDto.getName());
        }
        return commentDto;
    }
}