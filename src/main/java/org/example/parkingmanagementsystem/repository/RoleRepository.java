package org.example.parkingmanagementsystem.repository;

import org.example.parkingmanagementsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
