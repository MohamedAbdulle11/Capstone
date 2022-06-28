package com.app.gamelibrarymanagement.repository;

import com.app.gamelibrarymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(
            value = "SELECT * FROM users u WHERE u.username = ?1",
            nativeQuery = true)
    Optional<User> findByUsername(String username);

    //Find user by email from the database.
    @Query(
            value = "SELECT * FROM users u WHERE u.email = ?1",
            nativeQuery = true)
    Optional<User> findUserByEmail(String email);

    //Getting all the customers from the database, except admins
    @Query(
            value = "SELECT * FROM users u WHERE u.role_id = ?1",
            nativeQuery = true)
    List<User> getAllCustomers(Integer roleId);

}
