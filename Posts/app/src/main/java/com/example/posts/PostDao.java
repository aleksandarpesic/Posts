package com.example.posts;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    List<Post> getAll();

    @Query("SELECT * FROM post WHERE id =:idpost")
    Post getById(int idpost);

    @Query("SELECT * FROM post WHERE title =:t")
    Post getByTitle(String t);

    @Query("SELECT MAX(id) FROM post")
    int getLastId();

    @Insert
    void insertAll(Post... posts);

    @Delete
    void delete (Post post);

    @Query("DELETE FROM post")
    void nukeTable();

    @Query("DELETE FROM post WHERE id =:Id")
    void deleteById(int Id);

    @Query("DELETE FROM post WHERE title = :t")
    void deleteByTitle(String t);


}



