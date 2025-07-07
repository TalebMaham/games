package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gamesUP.gamesUP.enums.Role;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String password;

    private int age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonManagedReference("user-wishlists")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists;

    public User() {}

    public User(String name, String email, int age,  String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.age = age ; 
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public List<Wishlist> getWishlists() { return wishlists; }

    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setWishlists(List<Wishlist> wishlists) { this.wishlists = wishlists; }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
}


