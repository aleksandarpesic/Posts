package com.example.posts;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id =:iduser")
    User getById(int iduser);

    @Query("SELECT COUNT(*) FROM user")
    int getRowCount();

    @Insert
    void insertAll(User users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void nukeTable();
}
