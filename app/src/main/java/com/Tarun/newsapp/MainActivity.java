package com.Tarun.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingbar;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRV> categoryRVArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    private Button globalNews;
    private String APIString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIString = getString(R.string.API_KEY);
        newsRV = findViewById(R.id.NewsREC);
        categoryRV = findViewById(R.id.categoryREC);
        loadingbar = findViewById(R.id.LoadingBar);
        globalNews = findViewById(R.id.internationalBTN);
        articlesArrayList = new ArrayList<>();
        categoryRVArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

        globalNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent globalintent = new Intent(MainActivity.this, internationalnewsActivity.class);
                MainActivity.this.startActivity(globalintent);
            }
        });

    }

    private void getCategories(){

        categoryRVArrayList.add(new CategoryRV("All" , "https://images.unsplash.com/photo-1611159063981-b8c8c4301869?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80"));
        categoryRVArrayList.add(new CategoryRV("General" , "https://images.unsplash.com/photo-1457369804613-52c61a468e7d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=750&q=80"));
        categoryRVArrayList.add(new CategoryRV("Technology" , "https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80"));
        categoryRVArrayList.add(new CategoryRV("Science" , "https://images.unsplash.com/photo-1595500381966-eee2034aae48?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=625&q=80"));
        categoryRVArrayList.add(new CategoryRV("Health" , "https://images.unsplash.com/photo-1505751172876-fa1923c5c528?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
        categoryRVArrayList.add(new CategoryRV("Business" , "https://images.unsplash.com/photo-1591696205602-2f950c417cb9?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
        categoryRVArrayList.add(new CategoryRV("Sports" , "https://images.unsplash.com/photo-1589801258579-18e091f4ca26?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=776&q=80"));
        categoryRVArrayList.add(new CategoryRV("Entertainment" , "https://images.unsplash.com/photo-1499364615650-ec38552f4f34?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=666&q=80"));

        categoryRVAdapter.notifyDataSetChanged();

    }

    private void getNews(String category) {

        loadingbar.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String CategoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apikey=" + APIString;
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomain=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=" + APIString;
        String BaseURL = "https://newsapi.org/";

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseURL).addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;

        if(category.equals("All")){
            call = retrofitAPI.getAllnews(url);
        } else {
            call = retrofitAPI.getnewsbycategory(CategoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingbar.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();

                for(int i = 0; i < articles.size(); i++) {

                    articlesArrayList.add(new Articles(articles.get(i).getTitle() , articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                           articles.get(i).getUrl() , articles.get(i).getContent() ));

                }
                newsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Failed to load news!!!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onCategoryClick(int position) {

        String category = categoryRVArrayList.get(position).getCategory();
        getNews(category);

    }



}