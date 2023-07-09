package com.example.mediasoftjwt.mysql.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "userinfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @Column(name="login", nullable = false, unique = true)
    private String login;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="email", nullable = false, unique = true)
    private String email;
}
