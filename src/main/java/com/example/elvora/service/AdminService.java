package com.example.elvora.service;

import com.example.elvora.model.Admin;
import com.example.elvora.model.Login;
import com.example.elvora.repository.AdminRepository;
import com.example.elvora.repository.LoginRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final LoginRepository loginRepository;

    public AdminService(AdminRepository adminRepository,
                        LoginRepository loginRepository) {

        this.adminRepository = adminRepository;
        this.loginRepository = loginRepository;
    }

    public Admin registerAdmin(Admin admin, String password) {

        Admin savedAdmin = adminRepository.save(admin);

        Login login = new Login();

        login.setEmail(admin.getEmail());
        login.setPassword(password);
        login.setUsertype("admin");

        loginRepository.save(login);

        return savedAdmin;
    }

    public List<Admin> getAllAdmin() {

        return adminRepository.findAll();
    }

    public Admin getAdminByEmail(String email) {

        return adminRepository.findById(email).orElse(null);
    }

    public Admin updateAdmin(Admin admin) {

        return adminRepository.save(admin);
    }
    public boolean changePassword(String email,
                                  String oldPassword,
                                  String newPassword) {

        Login login = loginRepository.findById(email).orElse(null);

        if (login == null) {
            return false;
        }

        if (!login.getPassword().equals(oldPassword)) {
            return false;
        }

        login.setPassword(newPassword);

        loginRepository.save(login);

        return true;
    }
}