package com.example.user_system.specification;

import com.example.user_system.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> firstNameEquals(String firstName) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("firstName")), firstName.toLowerCase());
    }

    public static Specification<User> lastNameEquals(String lastName) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("lastName")), lastName.toLowerCase());
    }

}
