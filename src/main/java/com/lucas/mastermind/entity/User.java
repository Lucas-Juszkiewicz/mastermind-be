package com.lucas.mastermind.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "nick")
    @Size(max = 20, message = "Nick needs to have a maximum of 20 characters.")
    @NotBlank(message = "You have to provide your Nick!")
    private String nick;

    @Column(name = "email")
    private String email;

    @Column(name = "country")
    private String country;

    @Column(name = "password")
    @NotBlank(message = "You have to provide your Password!")
    private String password;

    @Column(name = "games")
    private Long games;

    @Column(name = "total")
    private Long total;

    @Lob
    @Column(name = "img")
    private byte[] img;

    @Column(name = "avatar")
    private int avatar;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }
}
