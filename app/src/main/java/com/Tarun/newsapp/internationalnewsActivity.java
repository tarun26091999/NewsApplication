package com.Tarun.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class internationalnewsActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    private RecyclerView globalnewsRV,globalcategoryRV;
    private ProgressBar globalloadingbar;
    private ArrayList<Articles> globalarticlesArrayList;
    private ArrayList<CategoryRV> globalcategoryRVArrayList;
    private CategoryRVAdapter globalcategoryRVAdapter;
    private NewsRVAdapter globalnewsRVAdapter;
    private String APIString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internationalnews);

        APIString = getString(R.string.API_KEY);
        globalnewsRV = findViewById(R.id.globalNewsREC);
        globalcategoryRV = findViewById(R.id.globalcategoryREC);
        globalloadingbar = findViewById(R.id.globalLoadingBar);
        globalarticlesArrayList = new ArrayList<>();
        globalcategoryRVArrayList = new ArrayList<>();
        globalnewsRVAdapter = new NewsRVAdapter(globalarticlesArrayList,this);
        globalcategoryRVAdapter = new CategoryRVAdapter(globalcategoryRVArrayList,this,this::onCategoryClick);
        globalnewsRV.setLayoutManager(new LinearLayoutManager(this));
        globalnewsRV.setAdapter(globalnewsRVAdapter);
        globalcategoryRV.setAdapter(globalcategoryRVAdapter);
        getGlobalCategories();
        getGlobalNews("All");
        globalnewsRVAdapter.notifyDataSetChanged();
    }

    private void getGlobalCategories(){

        globalcategoryRVArrayList.add(new CategoryRV("All" , "https://images.unsplash.com/photo-1504711434969-e33886168f5c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80"));
        globalcategoryRVArrayList.add(new CategoryRV("General" , "https://images.unsplash.com/photo-1526470608268-f674ce90ebd4?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=667&q=80"));
        globalcategoryRVArrayList.add(new CategoryRV("Technology" , "https://images.unsplash.com/photo-1518770660439-4636190af475?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80"));
        globalcategoryRVArrayList.add(new CategoryRV("Science" , "https://images.unsplash.com/photo-1567427018141-0584cfcbf1b8?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1500&q=80"));
        globalcategoryRVArrayList.add(new CategoryRV("Health" , "https://images.unsplash.com/photo-1505751172876-fa1923c5c528?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
        globalcategoryRVArrayList.add(new CategoryRV("Business" , "https://images.unsplash.com/photo-1474631245212-32dc3c8310c6?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=624&q=80"));
        globalcategoryRVArrayList.add(new CategoryRV("Sports" , "https://images.unsplash.com/photo-1486286701208-1d58e9338013?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
        globalcategoryRVArrayList.add(new CategoryRV("Entertainment" , "https://images.unsplash.com/photo-1486572788966-cfd3df1f5b42?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=752&q=80"));

        globalcategoryRVAdapter.notifyDataSetChanged();

    }

    private void getGlobalNews(String category) {

        globalloadingbar.setVisibility(View.VISIBLE);
        globalarticlesArrayList.clear();
        String CategoryURL = "https://newsapi.org/v2/top-headlines?category=" + category + "&apikey=" + APIString;
        String url = "https://newsapi.org/v2/top-headlines?excludeDomain=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=" + APIString;
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
                globalloadingbar.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();

                for(int i = 0; i < articles.size(); i++) {

                    globalarticlesArrayList.add(new Articles(articles.get(i).getTitle() , articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl() , articles.get(i).getContent() ));

                }
                globalnewsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                Toast.makeText(internationalnewsActivity.this, "Failed to load news!!!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onCategoryClick(int position) {

        String category = globalcategoryRVArrayList.get(position).getCategory();
        getGlobalNews(category);

    }
}