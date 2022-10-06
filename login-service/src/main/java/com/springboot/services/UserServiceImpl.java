package com.springboot.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.springboot.entity.AppRole;
import com.springboot.entity.AppUser;
import com.springboot.repository.AppRoleRepository;
import com.springboot.repository.AppUserRepository;
import com.springboot.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public AppUser getCurrentUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, Integer roleId) {
        AppUser user = userRepository.findByUsername(username);
        AppRole role = roleRepository.findById(roleId);
        user.getRoles().add(role);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        Collection<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> (GrantedAuthority) role::getName).collect(Collectors.toList());
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    @Override
    public AppUser getCurrentUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public int deleteUserById(Integer id) {
        return userRepository.deleteUserById(id);
    }

    @Override
    public void removeRoleFromUser(String username, Integer id) {
        AppUser user = userRepository.findByUsername(username);
        AppRole role = roleRepository.findById(id);
        user.getRoles().remove(role);
    }

    @Override
    public AppUser getUserByToken(String userToken) {
        userToken = userToken.substring("Bearer ".length() + JwtTokenUtils.preToken.length());
        String username = jwtTokenUtils.getUsernameFromToken(userToken);
        return userRepository.findByUsername(username);
    }
}
