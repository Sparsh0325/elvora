package com.example.elvora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="logindata")
public class Login{

        @Id
        private String email;

        private String password;
        private String usertype;

        public Login() {
        }

        public Login(String email, String password, String usertype) {
            this.email = email;
            this.password = password;
            this.usertype = usertype;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        @Override
        public String toString() {
            return "Login{" +
                    "email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", usertype='" + usertype + '\'' +
                    '}';
        }
    }

