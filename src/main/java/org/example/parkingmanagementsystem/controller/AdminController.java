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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/parking-management-system/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ParkingSpaceService parkingSpaceService;


//    @GetMapping("/login")
//    public String login() {
//        return "admin_login";
//    }

    @GetMapping("/register")
    public String register() {
        return "admin/admin_register";
    }

    @GetMapping("/home")
    public String home() {
        return "admin/admin_home";
    }


    @PostMapping("/register")
    public String registerUser (@ModelAttribute User user,Model model) {
        if (userService.userByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "Email already exists");
            return "admin/admin_register";
        }
        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userService.registerUser(user);
        return "login";
    }
    @GetMapping("/allUsers")
    public String allUsers(Model model,@RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("path.error", true);
            model.addAttribute("error", error);
        }
        List<User> users = userService.allUsers();
        model.addAttribute("users", users);
        model.addAttribute("title", "All Users");
        return "admin/manage_users";

    }

    @GetMapping("/pendingUsers")
    public String pendingUsers(Model model) {
        List<User> users = userService.pendingUsers();
        model.addAttribute("users", users);
        model.addAttribute("title", "Pending Users");
        return "admin/manage_users";
    }

    @GetMapping("/approvedUsers")
    public String approvedUsers(Model model) {
        List<User> users = userService.approvedUsers();
        model.addAttribute("users", users);
        model.addAttribute("title", "Approved Users");
        return "admin/manage_users";
    }

    @GetMapping("/user/{id}")
    public String userById(@PathVariable int id,Model model) {
        User user = userService.userById(id).orElse(null);
        if(user==null) {
            return "redirect:/parking-management-system/admin/allUsers?error=User not found";
        }
        model.addAttribute("user", user);
        return "admin/user_details";
    }

    @PutMapping("/approveUser/{id}")
    public String approveUser(@PathVariable int id) {
        Optional<User> user= userService.userById(id);
        if(user.isEmpty()) {
            return null;
        }
        user.get().setApproved(true);
        userService.updateUser(user.get());
        return "redirect:/parking-management-system/admin/allUsers";
    }

    @GetMapping("/allVehicles")
    public String allVehicles(Model model,@RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("path.error", true);
            model.addAttribute("error", error);
        }
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("title", "All Vehicles");
        model.addAttribute("twoWheelerSpace", parkingSpaceService.getTwoWheeler());
        model.addAttribute("fourWheelerSpace", parkingSpaceService.getFourWheeler());
        return "admin/manage_vehicles";
    }

    @GetMapping("/pendingVehicles")
    public String pendingVehicles(Model model) {

        List<Vehicle> vehicles = vehicleService.getPendingVehicles();
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("title", "Pending Vehicles");
        model.addAttribute("twoWheelerSpace", parkingSpaceService.getTwoWheeler());
        model.addAttribute("fourWheelerSpace", parkingSpaceService.getFourWheeler());
        return "admin/manage_vehicles";
    }

    @GetMapping("/approvedVehicles")
    public String approvedVehicles(Model model) {

        List<Vehicle> vehicles = vehicleService.getApprovedVehicles();
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("title", "Approved Vehicles");
        model.addAttribute("twoWheelerSpace", parkingSpaceService.getTwoWheeler());
        model.addAttribute("fourWheelerSpace", parkingSpaceService.getFourWheeler());
        return "admin/manage_vehicles";
    }

    @GetMapping("/vehicleById/{id}")
    public String vehicleById(@PathVariable int id,Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(id).orElse(null);

        if(vehicle==null) {
            return "redirect:/parking-management-system/admin/allVehicles?error=Vehicle not found";
        }
        model.addAttribute("vehicle",vehicle );
        model.addAttribute("twoWheelerSpace", parkingSpaceService.getTwoWheeler());
        model.addAttribute("fourWheelerSpace", parkingSpaceService.getFourWheeler());
        return "admin/vehicle_details";
    }

    @PutMapping("/approveVehicle/{id}")
    public String approveVehicle(@PathVariable int id,Model model) {
        Optional<Vehicle> vehicle= vehicleService.getVehicleById(id);
        if(vehicle.isEmpty()) {
            return "redirect:/parking-management-system/admin/getAllVehicles?error=Vehicle not found";
        }
        if (vehicle.get().getVehicleType().equals(VehicleType.TWO_WHEELER.toString()) && parkingSpaceService.getTwoWheeler() <= 0 || vehicle.get().getVehicleType().equals(VehicleType.FOUR_WHEELER.toString()) && parkingSpaceService.getFourWheeler() <= 0)
        {
            return "redirect:/parking-management-system/admin/allVehicles?error=Space not available";
        }
        if (vehicle.get().getVehicleType().equals(VehicleType.TWO_WHEELER.toString()))
        {
            parkingSpaceService.decreaseTwoWheeler();
        }
        else if (vehicle.get().getVehicleType().equals(VehicleType.FOUR_WHEELER.toString()))
        {
            parkingSpaceService.decreaseFourWheeler();
        }
        vehicle.get().setApproved(true);
        vehicleService.updateVehicle(vehicle.get());
        return "redirect:/parking-management-system/admin/allVehicles";
    }

}

