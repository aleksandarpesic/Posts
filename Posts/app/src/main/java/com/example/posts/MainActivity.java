package com.example.posts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    AppDatabase db;
    UserDao userDao;
    PostDao postDao;

    public  PostAdapter postAdapter;
    public static ListView listView;

    public static final String EXTRA_MESSAGE = "myposttitle.MESSAGE";
    public static final String EXTRA_MESSAGE2 = "myspendtime.MESSAGE";
    public static final int REQUEST_CODE1=1;

    private int spendSeconds=0;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        postDao = db.postDao();

        //add listview
        postAdapter=new PostAdapter(this);
        listView=(ListView)findViewById(R.id.list_view_posts);
        listView.setAdapter(postAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //get title on clicked item
                TextView name=view.findViewById(R.id.list_item_title);
                String itemValue=name.getText().toString();

                startDisplayPostActivity(itemValue);
            }
        });
        //if DB empty fetch data for first start
        if(postDao.getLastId()==0){
            try {
                addPost();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(); }
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
    }
    @Override
    protected void onResume() {
        super.onResume();
        timer=new Timer();
        startTimmer();
    }

    public void startTimmer(){
        int delay=0;
        int periodSecs=5;

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spendSeconds+=periodSecs;
                            //System.out.println("spendSeconds "+spendSeconds+" minutes: "+spendSeconds/60);

                            if(spendSeconds>60*5) { //every 5mins fetch data
                                try {
                                    addPost();
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();}
                                spendSeconds=0;
                            }

                        }
                    });
                }
            }, delay, periodSecs*1000); //periodSecs*1000 is in ms to run
    }

    //start another activity to show post details on click on listview item
    public void startDisplayPostActivity(String post_title){
        if(!post_title.equals("")) {
            Intent intent = new Intent(this, DisplayPostActivity.class);
            intent.putExtra(EXTRA_MESSAGE, post_title);
            intent.putExtra(EXTRA_MESSAGE2, spendSeconds);
            startActivityForResult(intent, REQUEST_CODE1);
        }
    }
    public void notifyDataChange(){
        postAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        TextView textView = findViewById(R.id.textView2);
        switch (requestCode) {
            case REQUEST_CODE1:
                if (resultCode == RESULT_OK) {
                    String postTitle=data.getStringExtra(EXTRA_MESSAGE);
                    spendSeconds=data.getIntExtra(EXTRA_MESSAGE2,0);
                    postDao.deleteByTitle(postTitle);
                    notifyDataChange();
                }
                if (resultCode == RESULT_CANCELED) {
                    spendSeconds=data.getIntExtra(EXTRA_MESSAGE2,0);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addPost(View view) throws IOException, InterruptedException {
        addPost();
    }
    //fetch data, parse, and add to DB
    public void addPost() throws IOException, InterruptedException{
        //fetch data to one big string from posts
        fetchWebData fetch_Data = new fetchWebData("https://jsonplaceholder.typicode.com/posts");
        Thread thread = new Thread(fetch_Data);
        thread.start();
        thread.join();
        String postsDataFromWeb=fetch_Data.getWebData();

        //fetch data to one big string from users
        fetch_Data.setUrlAdress("https://jsonplaceholder.typicode.com/users/");
        Thread thread2 = new Thread(fetch_Data);
        thread2.start();
        thread2.join();
        String usersDataFromWeb=fetch_Data.getWebData();

        //parse big string to atributes and insert to db
        PostsUsersDB db_access=new PostsUsersDB(this, postsDataFromWeb, usersDataFromWeb);
        db_access.deleteAllUsersPosts();
        db_access.Parse_override_UsersToDB();
        db_access.Parse_override_PostToDB();

        //db_access.showAllUsers();
        //db_access.showAllPosts();

        notifyDataChange();
        Toast.makeText(this,"All posts fetched and added to list",Toast.LENGTH_SHORT).show();
    }

}