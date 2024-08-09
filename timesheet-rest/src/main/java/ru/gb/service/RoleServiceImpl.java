package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.Role;
import ru.gb.model.User;
import ru.gb.repository.RoleRepository;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository repository;

    @Override
    public Set<Role> findAllRoles(User user) {
        return repository.findAllRoles(user);
    }

}
