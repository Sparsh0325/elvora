package com.example.elvora.service;

import com.example.elvora.dto.LoginDto;
import com.example.elvora.model.Login;
import com.example.elvora.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public String checkLogin(LoginDto dto) {

        Optional obj = loginRepository.findById(dto.getEmail());

        String ut = null;

        if (!obj.isEmpty()) {

            Login lgn = (Login) obj.get();

            if (lgn.getPassword().equals(dto.getPassword())) {

                ut = lgn.getUsertype();
            }
        }

        return ut;
    }
}