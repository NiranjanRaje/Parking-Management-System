package org.example.parkingmanagementsystem.service;


import org.example.parkingmanagementsystem.VehicleType;
import org.example.parkingmanagementsystem.model.ParkingSpace;
import org.example.parkingmanagementsystem.model.Vehicle;
import org.example.parkingmanagementsystem.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    public Optional<Vehicle> getVehicleById(long id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getVehiclesByUserId(long id) {
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getUser().getId() == id)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getPendingVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> !vehicle.isApproved())
                .collect(Collectors.toList());
    }

    public List<Vehicle> getApprovedVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(Vehicle::isApproved)
                .collect(Collectors.toList());
    }
    public String saveVehicle(Vehicle vehicle) {
        if (vehicle.getVehicleType().equals(VehicleType.TWO_WHEELER.toString())) {
            int numberOfTwoWheeler = vehicleRepository.findAll().stream()
                    .filter(vehicle1 -> vehicle1.getUser().getId() == vehicle.getUser().getId())
                    .filter(vehicle1 -> vehicle1.getVehicleType().equals(VehicleType.TWO_WHEELER.toString()))
                    .collect(Collectors.toList()).size();
            if (numberOfTwoWheeler >= 1) {
                return "No more than one two wheeler can be registered for a participant";
            }
        }
        else if (vehicle.getVehicleType().equals(VehicleType.FOUR_WHEELER.toString())) {
            int numberOfFourWheeler = vehicleRepository.findAll().stream()
                    .filter(vehicle1 -> vehicle1.getUser().getId() == vehicle.getUser().getId())
                    .filter(vehicle1 -> vehicle1.getVehicleType().equals(VehicleType.FOUR_WHEELER.toString()))
                    .collect(Collectors.toList()).size();
            if (numberOfFourWheeler >= 1) {
                return "No more than one four wheeler can be registered for a participant";
            }
        }
        if (vehicle.getVehicleType().equals(VehicleType.TWO_WHEELER.toString()) && parkingSpaceService.getTwoWheeler() <= 0 || vehicle.getVehicleType().equals(VehicleType.FOUR_WHEELER.toString()) && parkingSpaceService.getFourWheeler() <= 0) {
            return  "Space not available";
        }

        if (vehicle.getVehicleType().equals(VehicleType.TWO_WHEELER.toString()) && parkingSpaceService.getTwoWheeler() <= 0 || vehicle.getVehicleType().equals(VehicleType.FOUR_WHEELER.toString()) && parkingSpaceService.getFourWheeler() <= 0) {
            return  "Space not available";
        }
        vehicleRepository.save(vehicle);

        return "";
    }

    public void deleteVehicle(long id) {
        vehicleRepository.deleteById(id);
    }
    public Vehicle updateVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
