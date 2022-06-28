package com.app.gamelibrarymanagement.model;

import javax.persistence.*;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
//    private boolean borrowedStatus;
    private int quantity;
    @Column(nullable = true, length = 64)
    private String photo;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;
        return "/game-photos/" + id + "/" + photo;
    }

    public Game() {
    }

    public Game(Integer id, String name, String description, int quantity, String photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
