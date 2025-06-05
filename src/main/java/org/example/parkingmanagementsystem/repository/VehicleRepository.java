package org.example.parkingmanagementsystem.repository;

import org.example.parkingmanagementsystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
