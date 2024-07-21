package ru.gb.service;

import ru.gb.model.Role;
import ru.gb.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    Optional<User> findByLogin(String login);

    Set<Role> findAllRoles(User user);

}
