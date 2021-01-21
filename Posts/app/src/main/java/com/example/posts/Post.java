package com.example.posts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity =  User.class,
        parentColumns = "id",
        childColumns = "userId"))
public class Post {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="userId")
    public int user_id;

    @ColumnInfo(name="title")
    public String title;

    public String body;
}
