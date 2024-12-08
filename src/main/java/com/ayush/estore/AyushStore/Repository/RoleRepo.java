package com.ayush.estore.AyushStore.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.estore.AyushStore.entities.Role;

public interface RoleRepo extends JpaRepository<Role, String> {

    Optional<Role> findByName(String name);

}
