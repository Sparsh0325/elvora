package com.example.elvora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admindata")
public class Admin {



        @Id
        private String email;

        private String adminname;
        private String address;
        private long contact;

        public Admin() {
        }

        public Admin(String adminname, String address, long contact, String email) {
            this.adminname = adminname;
            this.address = address;
            this.contact = contact;
            this.email = email;
        }

        public String getAdminname() {
            return adminname;
        }

        public void setAdminname(String adminname) {
            this.adminname = adminname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getContact() {
            return contact;
        }

        public void setContact(long contact) {
            this.contact = contact;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }


        @Override
        public String toString() {
            return "Admin{" +
                    "adminname='" + adminname + '\'' +
                    ", address='" + address + '\'' +
                    ", contact=" + contact +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

