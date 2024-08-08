package ru.gb.service;

import ru.gb.model.Role;
import ru.gb.model.User;

import java.util.Set;

public interface RoleService {

    Set<Role> findAllRoles(User user);

}
