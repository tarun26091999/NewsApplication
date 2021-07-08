package com.Tarun.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class globalDetailedNewsActivity extends AppCompatActivity {

    String gcontent,gdesc,gtitle,gimageurl, gurl;
    private TextView gtitleTV, gsubdescTV, gcontentTV;
    private ImageView gnewsIV;
    private Button gnewsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_detailed_news);

        gtitle = getIntent().getStringExtra("title");
        gcontent = getIntent().getStringExtra("content");
        gdesc = getIntent().getStringExtra("desc");
        gimageurl = getIntent().getStringExtra("image");
        gurl = getIntent().getStringExtra("url");

        gtitleTV = findViewById(R.id.globaldetailTitle);
        gsubdescTV = findViewById(R.id.globaldetaildesc);
        gcontentTV = findViewById(R.id.globaldetailcontent);
        gnewsIV = findViewById(R.id.globaldetailedIV);
        gnewsBtn = findViewById(R.id.globaldetailBTN);
        gtitleTV.setText(gtitle);
        gsubdescTV.setText(gdesc);
        gcontentTV.setText(gcontent);
        Picasso.get().load(gimageurl).into(gnewsIV);

        gnewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(gurl));
                startActivity(intent);
            }
        });

    }
}