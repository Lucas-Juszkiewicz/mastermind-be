package com.lucas.mastermind.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nick")
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

    @Column(name = "img")
    private String img;

    @Column(name = "avatar")
    private int avatar;
}
