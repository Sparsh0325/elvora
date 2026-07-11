package com.example.elvora.service;

import com.example.elvora.model.Login;
import com.example.elvora.model.User;
import com.example.elvora.repository.LoginRepository;
import com.example.elvora.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;

    // TODO: inject your real BudgetRepository / TransactionRepository (or their
    // services) here once you share those files, then replace the placeholder
    // logic inside getAccountSummary() below with real sums for this user_id.
    // Example shape, once wired:
    //
    // private final BudgetRepository budgetRepository;
    // private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, LoginRepository loginRepository) {
        this.userRepository = userRepository;
        this.loginRepository = loginRepository;
    }

    public User registerUser(User user, String password) {
        User savedUser = userRepository.save(user);
        Login login = new Login();
        login.setEmail(user.getEmail());
        login.setPassword(password);
        login.setUsertype("user");
        loginRepository.save(login);
        return savedUser;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUserById(int user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(int user_id) {
        User user = userRepository.findById(user_id).orElse(null);
        if (user != null) {
            loginRepository.deleteById(user.getEmail());
            userRepository.delete(user);
        }
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
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

    /** Call this whenever a logged-in user hits any /user/** route, to keep "Last Active" accurate. */
    public void touchLastActive(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setLastActiveAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
