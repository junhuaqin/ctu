package com.qfg.ctu.dao.pojo;

import java.time.LocalDateTime;

/**
 * Created by rbtq on 7/26/16.
 */
public class User {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
