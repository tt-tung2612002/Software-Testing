package com.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.entity.AppRole;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByName(String name);

    AppRole findById(Integer id);

    Boolean existsByName(String name);
}
