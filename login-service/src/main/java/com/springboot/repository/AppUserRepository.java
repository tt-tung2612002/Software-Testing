package com.springboot.repository;

import java.util.List;

import com.springboot.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);

    AppUser findById(Integer id);

    @Procedure(procedureName = "getUserWithFilter")
    List<AppUser> findAllUserWithFilter(String filter);

    @Procedure(procedureName = "deleteUser")
    int deleteUserById(Integer id);

    Long deleteById(Integer id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
