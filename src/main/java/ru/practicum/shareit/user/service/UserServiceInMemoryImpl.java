package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyRegisteredException;
import ru.practicum.shareit.exception.EmptyFieldException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inmemory.UserRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.dto.mapper.UserMapper.toUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceInMemoryImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new EmptyFieldException("Email is empty");
        }
        log.info("Creating user: {}", userDto);
        return toUserDto(userRepository.create(toUser(userDto)));
    }

    @Override
    public UserDto getById(long id) {
        log.info("Getting user by Id: {}", id);
        return toUserDto(checkingId(id));
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Getting all users");
        return userRepository.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto) {
        checkingId(userDto.getId());
        checkingUser(toUser(userDto));
        log.info("Updating user: {}", userDto);

        User user = userRepository.getById(userDto.getId());
        userDto.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        userDto.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());

        return toUserDto(userRepository.update(toUser(userDto)));
    }

    @Override
    public void delete(long id) {
        checkingId(id);
        log.info("Deleting user by id: {}", id);
        userRepository.delete(id);
    }

    private User checkingId(long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new EntityNotFoundException("There is no User with id: " + id);
        }
        return user;
    }

    private void checkingUser(User user) {
        User userByEmail = userRepository.getByEmail(user.getEmail());
        if (userByEmail != null && !Objects.equals(user.getId(), userByEmail.getId())) {
            throw new EmailAlreadyRegisteredException("Email registered to other User");
        }
    }
}