package com.app.gamelibrarymanagement.repository;

import com.app.gamelibrarymanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    //Finding the role from the database by using its name
    Optional<Role> findByName(String name);
}
