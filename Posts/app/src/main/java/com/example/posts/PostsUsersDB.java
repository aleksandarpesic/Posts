package com.example.posts;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostsUsersDB {
    private String postsDataFromWeb;
    private String usersDataFromWeb;

    AppDatabase db;
    PostDao postDao;
    UserDao userDao;
    Context context;

    public PostsUsersDB(Context c, String postsData, String usersData){
        context=c;
        postsDataFromWeb=postsData;
        usersDataFromWeb=usersData;
    }

    public void Parse_override_PostToDB(){
        db = AppDatabase.getInstance(context);
        postDao = db.postDao();
        postDao.nukeTable();
        Post post=new Post();

        //uzimam elemente iz stringa

        Scanner scanner=new Scanner(postsDataFromWeb);
        String line;
        String tmp;
        while(scanner.hasNextLine()) {
            line=scanner.nextLine();
            //System.out.println(line);
            String uid="\"userId\": " ;
            String id1="\"id\": " ;
            String title1="\"title\": ";
            String body1="\"body\": ";

            if(line.contains(uid)) {
                int first=line.lastIndexOf(uid)+(uid).length();
                int last=line.lastIndexOf(",");
                post.user_id=(Integer.parseInt( line.substring(first, last)) );
            }
            else if(line.contains(id1)) {
                int first=line.lastIndexOf(id1)+(id1).length();
                int last=line.lastIndexOf(",");
                post.id=Integer.parseInt( line.substring(first, last));
            }
            else if (line.contains(title1)) {
                int first=line.lastIndexOf(title1)+(title1).length()+1;
                int last=line.lastIndexOf("\"");
                post.title=( line.substring(first, last) );
            }
            else if (line.contains(body1)) {
                int first=line.lastIndexOf(body1)+(body1).length()+1;
                int last=line.lastIndexOf("\"");
                post.body=( line.substring(first, last) );
                postDao.insertAll(post);
            }

        }

    }

    public void showAllPosts(){
        db = AppDatabase.getInstance(context);
        postDao = db.postDao();

        Post post=new Post();
        int numofPosts=postDao.getLastId();
        for(int i=1;i<=numofPosts;i++){
            if(postDao.getById(i)!=null) {
                post = postDao.getById(i);
                System.out.println(post.id);
                //System.out.println(post.user_id);
                System.out.println(post.title);
                System.out.println(post.body);
                System.out.println();
            }
        }
        System.out.println("End of posts");
    }

    public void deleteAllPosts(){
        db = AppDatabase.getInstance(context);
        postDao = db.postDao();
        postDao.nukeTable();
    }

    public void Parse_override_UsersToDB(){
        db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        userDao.nukeTable();
        User user=new User();

        Scanner scanner=new Scanner(usersDataFromWeb);
        String line;
        String tmp;
        while(scanner.hasNextLine()) {
            line=scanner.nextLine();
            //System.out.println(line);
            String id1="\"id\": " ;
            String name1="\"name\": " ;
            String username1="\"username\": ";
            String email1="\"email\": ";
            String phone1="\"phone\": ";
            String website1="\"website\": ";

            if(line.contains(id1)) {
                int first=line.lastIndexOf(id1)+(id1).length();
                int last=line.lastIndexOf(",");
                user.id=(Integer.parseInt( line.substring(first, last)) );
            }
            else if (line.contains(name1)) {
                int first=line.lastIndexOf(name1)+(name1).length()+1;
                int last=line.lastIndexOf("\"");
                user.name=( line.substring(first, last) );
            }
            else if (line.contains(username1)) {
                int first=line.lastIndexOf(username1)+(username1).length()+1;
                int last=line.lastIndexOf("\"");
                user.username=( line.substring(first, last) );
            }
            else if (line.contains(email1)) {
                int first=line.lastIndexOf(email1)+(email1).length()+1;
                int last=line.lastIndexOf("\"");
                user.email=( line.substring(first, last) );
            }
            else if (line.contains(phone1)) {
                int first=line.lastIndexOf(phone1)+(phone1).length()+1;
                int last=line.lastIndexOf("\"");
                user.phone=( line.substring(first, last) );
            }
            else if (line.contains(website1)) {
                int first=line.lastIndexOf(website1)+(website1).length()+1;
                int last=line.lastIndexOf("\"");
                user.web=( line.substring(first, last) );

                line=scanner.nextLine();line=scanner.nextLine();line=scanner.nextLine();

                userDao.insertAll(user);
            }

        }
    }

    public void showAllUsers(){
        db = AppDatabase.getInstance(context);
        userDao = db.userDao();

        User user=new User();
        int numofUsers=userDao.getRowCount();
        for(int i=1;i<=numofUsers;i++){
            user=userDao.getById(i);
            System.out.println(user.id);
            System.out.println(user.name);
            System.out.println(user.username);
            System.out.println(user.email);
            System.out.println(user.phone);
            System.out.println(user.web);
            System.out.println();
        }
        System.out.println("End of users");
    }

    public void deleteAllUsersPosts(){
        deleteAllPosts();
        db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        userDao.nukeTable();
    }

}
