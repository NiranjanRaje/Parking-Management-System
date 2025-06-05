package org.example.parkingmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
//@Getter, @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_space")
public class ParkingSpace {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "two_wheeler")
    private int twoWheeler ;

    @Column(name = "four_wheeler")
    private int fourWheeler ;

}