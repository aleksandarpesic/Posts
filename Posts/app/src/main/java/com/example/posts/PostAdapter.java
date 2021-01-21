package com.example.posts;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintSet;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;

public class PostAdapter extends BaseAdapter {
    AppDatabase db;
    PostDao postDao;
    public Context context;

    public PostAdapter(Context context){
        this.context=context;

        db = AppDatabase.getInstance(context);
        postDao = db.postDao();
    }

    @Override
    public int getCount() {  return postDao.getLastId();  }

    @Override
    public Object getItem(int position) {   return postDao.getById(position);    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (postDao.getById(position+1 )==null){ //set blank listview item
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }
            TextView title = convertView.findViewById(R.id.list_item_title);
            TextView text = convertView.findViewById(R.id.list_item_text);
            title.setMaxLines(1);
            text.setMaxLines(1);

            title.setText("");
            text.setText("");
        }
        if (postDao.getById(position+1 )!=null) {  //set listview item
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }
            TextView title = convertView.findViewById(R.id.list_item_title);
            TextView text = convertView.findViewById(R.id.list_item_text);
            title.setMaxLines(1);
            text.setMaxLines(1);

            title.setText(postDao.getById(position+1).title);
            text.setText(postDao.getById(position+1).body);
        }

        return convertView;
    }
}
