package com.example.posts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DisplayPostActivity extends AppCompatActivity {
    AppDatabase db;
    UserDao userDao;
    PostDao postDao;
    public  Post post;
    public User user;

    String postTitle; //setbyIntent

    TextView textViewTitle;
    TextView textViewText;
    TextView textViewAuthorName;
    TextView textViewAuthorEmail;

    int spendSeconds2;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaypost);

        textViewTitle =findViewById(R.id.textView2);
        textViewText =findViewById(R.id.textView3);
        textViewAuthorName =findViewById(R.id.textView5);
        textViewAuthorEmail =findViewById(R.id.textView6);

        Intent intent = getIntent();
        postTitle = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        spendSeconds2=intent.getIntExtra(MainActivity.EXTRA_MESSAGE2,0);

        db = AppDatabase.getInstance(this);
        postDao = db.postDao();
        userDao=db.userDao();

        post=postDao.getByTitle(postTitle);

        textViewTitle.setText(post.title);
        textViewText.setText(post.body);

        int idAuthor=post.user_id; System.out.println(idAuthor);
        user=userDao.getById(idAuthor);
        textViewAuthorName.setText("   Author name: "+user.name);
        textViewAuthorEmail.setText(" Author email: "+user.email);
    }

    //show simple dialog, if click yes, run function clickedOKForDelete() and delete this post in MainActivity
    public void deletePost(View view){
        showSimleDialog_ConfirmDelete();
    }

    private void showSimleDialog_ConfirmDelete() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Delete post");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickedOKForDelete();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickedCancel();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void clickedCancel() {
        Toast.makeText(this,"You clicked cancel",Toast.LENGTH_LONG).show();
        Intent intent=new Intent();
        intent.putExtra(MainActivity.EXTRA_MESSAGE2,spendSeconds2);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
    private void clickedOKForDelete(){
        Intent intent=new Intent();
        intent.putExtra(MainActivity.EXTRA_MESSAGE,postTitle);
        intent.putExtra(MainActivity.EXTRA_MESSAGE2,spendSeconds2);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void btn_Back(View view) {
        clickedCancel();
    }

    @Override
    protected void onPause() {
        timer.cancel(); timer.purge();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer=new Timer(); startTimmer();
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
                        spendSeconds2+=periodSecs;
                    }
                });
            }
        }, delay, periodSecs*1000); //periodSecs*1000 is in ms to run
    }

}
