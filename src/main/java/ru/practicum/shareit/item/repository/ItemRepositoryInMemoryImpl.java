package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryInMemoryImpl implements ItemRepository {

    private final UserRepository userRepository;
    private final Map<Long, List<Item>> items = new HashMap<>();
    private final Map<Long, Item> itemsStorage = new HashMap<>();
    private long itemId = 0;


    @Override
    public Item create(Item item, long userId) {
        item.setId(++itemId);
        item.setOwner((userRepository.getById(userId)));
        items.compute(userId, (ownerId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        log.debug("Adding item: {}", item);
        itemsStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item, long userId) {
        log.debug("Updating item : {}", item);
        if (userId != item.getOwner().getId()) {
            throw new EntityNotFoundException("Owner id is incorrect!");
        }
        itemsStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        log.debug("Getting item by id: {} ", itemId);
        return itemsStorage.get(itemId);
    }

    @Override
    public Collection<Item> getItemsByUserId(long userId) {
        return items.get(userId);
    }

    @Override
    public Collection<Item> getItemsBySearch(String text) {
        Collection<Item> availableItems = new ArrayList<>();
        for (long userId : items.keySet()) {
            availableItems.addAll(items.get(userId).stream()
                    .filter(x -> x.getAvailable().equals(true))
                    .filter(x -> x.getName().toLowerCase().contains(text) ||
                            x.getDescription().toLowerCase().contains(text))
                    .collect(Collectors.toList()));
        }
        return availableItems;
    }

    private int findItemIndexInList(long itemId, long userId) {
        return IntStream.range(0, items.get(userId).size())
                .filter(i -> items.get(userId).get(i).getId() == itemId)
                .findFirst()
                .orElse(-1);
    }
}