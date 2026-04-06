package com.pickup.organizer.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.pickup.organizer.entity.Player;

public class PlayerSpecifications {

    public static Specification<Player> nameStartsWith(String name) {
        return (root, query, cb) ->
            name == null
            ? null
            : cb.like(root.get("name"), name + "%");
    }

    public static Specification<Player> isAfter(LocalDate bornAfter) {
        return (root, query, cb) ->
            bornAfter == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("birthDate"), bornAfter);
    }
}
