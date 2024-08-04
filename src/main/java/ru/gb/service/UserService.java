package ru.gb.service;

import ru.gb.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByLogin(String login);

}
