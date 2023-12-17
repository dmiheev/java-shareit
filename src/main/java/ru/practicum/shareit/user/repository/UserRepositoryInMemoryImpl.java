package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailAlreadyRegisteredException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class UserRepositoryInMemoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> uniqueEmailsSet = new HashMap<>();
    private long userId = 0;

    @Override
    public User create(User user) {
        if (uniqueEmailsSet.containsKey(user.getEmail())) {
            throw new EmailAlreadyRegisteredException("User with this email is already exists!");
        }
        log.debug("Creating user: {}", user);
        user.setId(++userId);

        users.put(user.getId(), user);
        uniqueEmailsSet.put(user.getEmail(), user);
        return users.get(user.getId());
    }

    @Override
    public User getById(long id) {
        log.debug("Getting User by ID: {}", id);
        return users.get(id);
    }

    @Override
    public User getByEmail(String email) {
        log.debug("Getting User by Email: {}", email);
        return uniqueEmailsSet.get(email);
    }

    @Override
    public Collection<User> getAll() {
        log.debug("Getting all users");
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user) {
        if (!user.getEmail().equals(users.get(user.getId()).getEmail())) {
            if (!uniqueEmailsSet.containsKey(user.getEmail())) {
                uniqueEmailsSet.put(user.getEmail(), user);
                uniqueEmailsSet.remove(users.get(user.getId()).getEmail());
            } else {
                throw new EmailAlreadyRegisteredException("User with this email is already exists!");
            }
        }
        String currEmail = users.get(user.getId()).getEmail();
        log.debug("Updating user with id: {}, user: {}", user.getId(), user);
        uniqueEmailsSet.remove(currEmail);
        users.put(user.getId(), user);
        uniqueEmailsSet.put(user.getEmail(), user);
        return users.get(user.getId());
    }

    @Override
    public void delete(long id) {
        log.debug("Removing User with id: {}", id);
        uniqueEmailsSet.remove(users.get(id).getEmail());
        users.remove(id);
    }

}