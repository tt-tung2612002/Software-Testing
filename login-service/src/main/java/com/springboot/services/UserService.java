package com.springboot.services;

import com.springboot.entity.AppRole;
import com.springboot.entity.AppUser;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    AppUser getCurrentUserByName(String username);

    AppUser getCurrentUserById(Integer id);

    AppUser saveUser(AppUser user);

    AppUser getUserByToken(String token);

    int deleteUserById(Integer id);

    AppRole saveRole(AppRole role);

    void addRoleToUser(String username, Integer id);

    void removeRoleFromUser(String username, Integer id);

}
