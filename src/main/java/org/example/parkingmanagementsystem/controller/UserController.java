package org.example.parkingmanagementsystem.controller;


import org.example.parkingmanagementsystem.model.ParkingSpace;
import org.example.parkingmanagementsystem.VehicleType;
import org.example.parkingmanagementsystem.model.Role;
import org.example.parkingmanagementsystem.model.User;
import org.example.parkingmanagementsystem.model.Vehicle;
import org.example.parkingmanagementsystem.repository.RoleRepository;
import org.example.parkingmanagementsystem.service.ParkingSpaceService;
import org.example.parkingmanagementsystem.service.UserService;
import org.example.parkingmanagementsystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/parking-management-system/participant")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VehicleService vehicleService;


    @Autowired
    private ParkingSpaceService parkingSpaceService;



    @GetMapping("/register")
    public String register() {
        return "participant/participant_register";
    }

    @GetMapping("/home")
    public String home(Model model,@RequestParam(required = false) String error) {
        if (error!=null) {
            if (!error.isEmpty()) {
                model.addAttribute("path.error", true);
                model.addAttribute("error", error);
            }

        }
        model.addAttribute("twoWheelerSpace", parkingSpaceService.getTwoWheeler() );
        model.addAttribute("fourWheelerSpace", parkingSpaceService.getFourWheeler() );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> user = userService.userByEmail(email);
        List<Vehicle> vehicles = vehicleService.getVehiclesByUserId(user.get().getId());

        if (vehicles != null && !vehicles.isEmpty()) {
            model.addAttribute("vehicles", vehicles);
        }

        return "participant/participant_home";
    }


    @GetMapping("/addVehicle")
    public String addVehicle(Model model) {
        model.addAttribute("vehicleTypes", VehicleType.values());
        return "participant/add_vehicle";
    }

    @GetMapping("/editVehicle/{id}")
    public String editVehicle(@PathVariable int id,Model model) {
        Optional<Vehicle> vehicle= vehicleService.getVehicleById(id);
        if(vehicle.isEmpty()) {
            return "redirect:/parking-management-system/participant/home?error=Vehicle not found";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> user = userService.userByEmail(email);
        if (user.get().getId() != vehicle.get().getUser().getId()) {
            return "redirect:/parking-management-system/participant/home?error=You are not authorized to edit this vehicle";
        }
        model.addAttribute("vehicleTypes", VehicleType.values());
        model.addAttribute("vehicle", vehicle.get());
        return "participant/edit_vehicle_details";
    }

    @PostMapping("/register")
    public String registerUser (@ModelAttribute User user,Model model) {
        if (userService.userByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "Email already exists");
            return  "participant/participant_register";
        }
        Role role = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userService.registerUser(user);
        return "login";
    }


    @PostMapping("/addVehicle")
    public String addVehicleDetails (@ModelAttribute Vehicle vehicle,Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.userByEmail(email).orElse(null);
        if (user == null) {
            return  "redirect:/parking-management-system/participant/home?error=User not found";
        }
        vehicle.setUser(user);
        String message=vehicleService.saveVehicle(vehicle);
        return  "redirect:/parking-management-system/participant/home?error="+message;
    }

    @PutMapping("/editVehicle")
    public String editVehicleDetails (@ModelAttribute Vehicle vehicle,Model model) {
        if (vehicleService.getVehicleById(vehicle.getId()).get().isApproved()) {
            if (vehicle.getVehicleType().equals(VehicleType.TWO_WHEELER.toString()))
            {
                parkingSpaceService.increaseTwoWheeler();
            }
            if (vehicle.getVehicleType().equals(VehicleType.FOUR_WHEELER.toString()))
            {
                parkingSpaceService.increaseFourWheeler();
            }
        }
        Vehicle vehicle1=vehicleService.getVehicleById(vehicle.getId()).orElse(null);
        vehicle.setCreated(vehicle1.getCreated());
        vehicle.setLastUpdated(vehicle1.getLastUpdated());

        vehicleService.updateVehicle(vehicle);
        return "redirect:/parking-management-system/participant/home";
    }

    @DeleteMapping("/removeVehicleDetails/{id}")
    public String removeVehicleDetails(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.userByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/parking-management-system/participant/home?error=User not found";
        }
        Vehicle vehicle=vehicleService.getVehicleById(id).orElse(null);
        if(vehicle==null) {
            return "redirect:/parking-management-system/participant/home?error=Vehicle not found";
        }
        if (vehicle.getUser().getId() == user.getId()) {
            vehicleService.deleteVehicle(id);
            if (vehicle.isApproved()) {
                if (vehicle.getVehicleType().equals(VehicleType.TWO_WHEELER.toString()))
                {
                    parkingSpaceService.increaseTwoWheeler();
                }
                if (vehicle.getVehicleType().equals(VehicleType.FOUR_WHEELER.toString()))
                {
                    parkingSpaceService.increaseFourWheeler();
                }
            }
        }


       return "redirect:/parking-management-system/participant/home";
    }

}
