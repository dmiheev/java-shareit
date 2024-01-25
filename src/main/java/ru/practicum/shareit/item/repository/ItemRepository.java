package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner_Id(Long ownerId, Pageable page);

    @Query("select i from Item i where i.owner.id = :ownerId")
    List<Item> findByOwner_Id_WithoutPageable(Long ownerId);

    @Query("select i from Item i where i.available = true AND (upper(i.name) like upper(concat('%', :text, '%')) OR " +
            "upper(i.description) like upper(concat('%', :text, '%')))")
    List<Item> search(String text, Pageable page);

}