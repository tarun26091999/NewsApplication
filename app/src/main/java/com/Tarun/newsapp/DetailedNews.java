package com.Tarun.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedNews extends AppCompatActivity {

    String content,desc,title,imageurl, url;
    private TextView titleTV, subdescTV, contentTV;
    private ImageView newsIV;
    private Button newsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        desc = getIntent().getStringExtra("desc");
        imageurl = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        titleTV = findViewById(R.id.detailTitle);
        subdescTV = findViewById(R.id.detaildesc);
        contentTV = findViewById(R.id.detailcontent);
        newsIV = findViewById(R.id.detailedIV);
        newsBtn = findViewById(R.id.detailBTN);
        titleTV.setText(title);
        subdescTV.setText(desc);
        contentTV.setText(content);
        Picasso.get().load(imageurl).into(newsIV);

        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }
}