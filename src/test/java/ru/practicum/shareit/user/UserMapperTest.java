package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserMapperTest {

    @Test
    void toUserDto() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@Mail.ru")
                .build();
        User userFromConstructor = new User(1L, "name", "mail@mail.ru");

        UserDto userDto = UserMapper.toUserDto(user);
        UserDto userDtoConstructor = UserMapper.toUserDto(userFromConstructor);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(userDtoConstructor.getName(), userFromConstructor.getName());
    }

    @Test
    void toUser() {
        UserDto userDto = UserDto.builder()
                .id(2L)
                .email("email@mail.ru")
                .name("name")
                .build();
        User user = UserMapper.toUser(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void toUserUpdate() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@Mail.ru")
                .build();
        UserDto userDto = UserDto.builder()
                .id(2L)
                .email("email@mail.ru")
                .name("name")
                .build();

        User userToUpdate = UserMapper.toUserUpdate(userDto, user);


        assertEquals(userToUpdate.getId(), userDto.getId());
        assertEquals(userToUpdate.getName(), user.getName());
        assertEquals(userToUpdate.getEmail(), userDto.getEmail());
    }
}