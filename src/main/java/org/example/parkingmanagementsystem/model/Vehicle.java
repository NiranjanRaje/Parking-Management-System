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
@Table(name = "vehicle")
public class Vehicle extends BaseEntity {


    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(name = "vehicle_number", nullable = false)
    private long vehicleNumber;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "model")
    private String model;

    @Column(name = "color")
    private String color;
}

