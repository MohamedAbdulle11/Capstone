package com.app.gamelibrarymanagement.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "First Name shouldn't be empty")
    private String firstName;
    @NotBlank
    private String lastName;
    @Column(unique = true)
    @NotBlank(message = "Username shouldn't be empty")
    private String username;
    @Column(unique = true)
    @NotBlank(message = "Email shouldn't be empty")
    @Email
    private String email;
    @NotBlank(message = "Password shouldn't be empty")
    private String password;
    @NotBlank
    private String address;
    @NotNull(message = "Age shouldn't be null")
    private int age;

    @OneToOne
    private Role role;

    @ManyToMany(targetEntity = Game.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    List<Game> borrowedGamesList = new ArrayList<>();

    @ManyToMany(targetEntity = Game.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    List<Game> purchasedGamesList = new ArrayList<>();

    public User() {
    }

    public User(Integer id, String firstName, String lastName, String username, String email, String password, String address, int age, Role role, List<Game> borrowedGamesList, List<Game> purchasedGamesList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.age = age;
        this.role = role;
        this.borrowedGamesList = borrowedGamesList;
        this.purchasedGamesList = purchasedGamesList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Game> getBorrowedGamesList() {
        return borrowedGamesList;
    }

    public void setBorrowedGamesList(List<Game> borrowedGamesList) {
        this.borrowedGamesList = borrowedGamesList;
    }

    public List<Game> getPurchasedGamesList() {
        return purchasedGamesList;
    }

    public void setPurchasedGamesList(List<Game> purchasedGamesList) {
        this.purchasedGamesList = purchasedGamesList;
    }
}
