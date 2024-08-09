package com.lucas.mastermind.DTO;

public class UserAuth {

    private String firstName = "";
    private String lastName = "";
    private String email;
    private String username;
    private String userId;
    private String roles = "";

    public UserAuth(String email, String userNick, String userId) {
        this.email = email;
        this.username = userNick;
        this.userId = userId;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "UserAuth{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
