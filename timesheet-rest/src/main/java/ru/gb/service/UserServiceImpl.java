package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.User;
import ru.gb.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

}
