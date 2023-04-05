package com.keyin.utils;
public class Patron {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private String phone;

    public Patron(int id, String first_name, String last_name, String email, String address, String phone) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}