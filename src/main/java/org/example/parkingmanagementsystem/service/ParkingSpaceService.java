package org.example.parkingmanagementsystem.service;


import org.example.parkingmanagementsystem.model.ParkingSpace;
import org.example.parkingmanagementsystem.model.User;
import org.example.parkingmanagementsystem.repository.ParkingSpaceRepository;
import org.example.parkingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingSpaceService {


    @Autowired
    ParkingSpaceRepository parkingSpaceRepository;

    public int getFourWheeler() {
        ParkingSpace parkingSpace=parkingSpaceRepository.findById(1L).orElse(null);
        if (parkingSpace!=null)
        {
            return parkingSpace.getFourWheeler();
        }
        return 0;
    }

    public int getTwoWheeler() {
        ParkingSpace parkingSpace=parkingSpaceRepository.findById(1L).orElse(null);
        if (parkingSpace!=null)
        {
            return parkingSpace.getTwoWheeler();
        }
        return 0;
    }

    public boolean decreaseFourWheeler() {
        ParkingSpace parkingSpace=parkingSpaceRepository.findById(1L).orElse(null);
        if (parkingSpace!= null && parkingSpace.getFourWheeler()>0)
        {
            parkingSpace.setFourWheeler(parkingSpace.getFourWheeler()-1);
            parkingSpaceRepository.save(parkingSpace);
            return true;
        }
        return false;
    }

    public boolean decreaseTwoWheeler() {
        ParkingSpace parkingSpace=parkingSpaceRepository.findById(1L).orElse(null);
        if (parkingSpace!= null && parkingSpace.getTwoWheeler()>0)
        {
            parkingSpace.setTwoWheeler(parkingSpace.getTwoWheeler()-1);
            parkingSpaceRepository.save(parkingSpace);
            return true;
        }
        return false;
    }

    public boolean increaseFourWheeler() {

        ParkingSpace parkingSpace=parkingSpaceRepository.findById(1L).orElse(null);
        if (parkingSpace!= null && parkingSpace.getFourWheeler()>0)
        {
            parkingSpace.setFourWheeler(parkingSpace.getFourWheeler()+1);
            parkingSpaceRepository.save(parkingSpace);
            return true;
        }
        return false;
    }

    public boolean increaseTwoWheeler() {
        ParkingSpace parkingSpace=parkingSpaceRepository.findById(1L).orElse(null);
        if (parkingSpace!= null && parkingSpace.getTwoWheeler()>0)
        {
            parkingSpace.setTwoWheeler(parkingSpace.getTwoWheeler()+1);
            parkingSpaceRepository.save(parkingSpace);
            return true;
        }
        return false;
    }
}
