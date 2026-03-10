package com.santhosh.foodordering.model;

import jakarta.persistence.*;

@Entity
@Table(name="restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long rstid;
    private String name;
    private String address;
    private boolean active;

    @ManyToOne
    private  Users users;

    public Restaurant() {
    }

    public Long getRstid() {
        return rstid;
    }

    public void setRstid(Long rstid) {
        this.rstid = rstid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
