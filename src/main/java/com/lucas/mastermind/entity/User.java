package com.lucas.mastermind.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    public User() {
    }

    public User(String nick, String email, String password) {
        this.nick = nick;
        this.email = email;
        this.password = password;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "nick", unique = true)
    @Size(max = 20, message = "Nick needs to have a maximum of 20 characters.")
    @NotBlank(message = "You have to provide your Nick!")
    private String nick;

    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(name = "country")
    private String country;

    @Column(name = "password")
    @NotBlank(message = "You have to provide your Password!")
    private String password;

    @Column(name = "total")
    private Long total;

    @Lob
    @Column(name = "img")
    private byte[] img;

    @Column(name = "avatar")
    private Long avatar;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<Game> games;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }
}
