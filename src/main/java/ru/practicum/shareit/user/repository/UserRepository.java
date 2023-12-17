package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {

    User create(User user);

    User getById(long id);

    User getByEmail(String email);

    Collection<User> getAll();

    User update(User user);

    void delete(long id);

}