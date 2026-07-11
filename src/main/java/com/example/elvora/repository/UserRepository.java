package com.example.elvora.repository;

import com.example.elvora.model.Admin;
import com.example.elvora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    
}
