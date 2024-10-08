package com.lucas.mastermind.DTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @Size(max = 20, message = "Nick needs to have a maximum of 20 characters.")
    @NotBlank(message = "You have to provide your Nick!")
    private String nick;

    @Email
    private String email;

    private String country;

    private Long total;

    @Lob
    private byte[] img;
    private Long avatar;

    private LocalDateTime registrationDate;

    public UserDTO(Long id, String nick, String email, String country, Long total, byte[] img, Long avatar, LocalDateTime registrationDate) {
        this.id = id;
        this.nick = nick;
        this.email = email;
        this.country = country;
        this.total = total;
        this.img = img;
        this.avatar = avatar;
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", nick='" + nick + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", total=" + total +
                ", img=" + Arrays.toString(img) +
                ", avatar=" + avatar +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
