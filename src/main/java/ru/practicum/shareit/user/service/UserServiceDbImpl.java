package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmptyFieldException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.mapper.UserMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceDbImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new EmptyFieldException("Email is empty");
        }
        log.info("Creating user : {}", userDto);
        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public UserDto getById(long id) {
        log.info("Getting user by Id: {}", id);
        User userFromRep = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no User with id: " + id));
        return toUserDto(userFromRep);
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Getting all users");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto) {
        log.info("Updating user: {}", userDto);

        User userToUpdate = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("There is no User with id: " + userDto.getId()));
        userToUpdate.setName(userDto.getName() != null ? userDto.getName() : userToUpdate.getName());
        userToUpdate.setEmail(userDto.getEmail() != null ? userDto.getEmail() : userToUpdate.getEmail());

        userRepository.save(userToUpdate);
        return toUserDto(userToUpdate);
    }

    @Override
    public void delete(long id) {
        User userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no User with id: " + id));
        log.info("Deleting user by id: {}", id);
        userRepository.deleteById(userFromDb.getId());
    }
}