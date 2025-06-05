package org.example.parkingmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
//@Getter, @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {



    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "company_name", nullable = false)
    private String company;

    @ManyToOne
    @JoinColumn(name = "role", nullable = false)
    private Role role;
}

