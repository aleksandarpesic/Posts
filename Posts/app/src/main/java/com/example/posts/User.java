package com.example.posts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="username")
    public String username;

    @ColumnInfo(name="email")
    public String email;

    @ColumnInfo(name="phone")
    public String phone;

    @ColumnInfo(name="website")
    public String web;


}
