package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;

@Getter
@Setter
@ToString
@Table(name = "users")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Email
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
