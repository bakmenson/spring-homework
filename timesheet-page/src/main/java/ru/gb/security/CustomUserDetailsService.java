package ru.gb.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

//@Component
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
public class CustomUserDetailsService {

//    private UserService userService;
//    private RoleService roleService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userService.findByLogin(username)
//                .orElseThrow(() -> new UsernameNotFoundException("There is not user with login '" + username + "'"));
//
//        Set<SimpleGrantedAuthority> roles = roleService.findAllRoles(user).stream()
//                .map(it -> new SimpleGrantedAuthority(it.getName()))
//                .collect(Collectors.toSet());
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getLogin(),
//                user.getPassword(),
//                roles
//        );
//    }

}
