package com.example.elvora.repository;

import com.example.elvora.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository  extends JpaRepository<Login, String> {
}
