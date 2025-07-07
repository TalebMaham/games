package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.enums.Role;

public class UserRegistrationRequest {

    private String name;
    private String email;
    private String password;
    private Role role;
    private int age ; 

    public UserRegistrationRequest() {}

    public UserRegistrationRequest(String name, String email, int age,  String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.age = age ; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
