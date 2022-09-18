package com.moengage.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moengage.newsapp.adapter.ArticleAdapter;
import com.moengage.newsapp.async.NewsHttpRequestTask;
import com.moengage.newsapp.interfaces.OnTaskDoneListener;
import com.moengage.newsapp.modal.ArticlesItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnTaskDoneListener {

    // setting the TAG for debugging purposes
    private static String TAG = "MainActivity";
    // declaring the views
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    // declaring an ArrayList of articles
    private ArrayList<ArticlesItem> mArticleList;
    private ArticleAdapter mArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assigning views to their ids
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_id);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
        // setting the recyclerview layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // initializing the ArrayList of articles
        mArticleList = new ArrayList<>();

        //calling api
        callNewsApi();
    }

    private void callNewsApi() {
        mArticleList.clear();
        String my_url = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";
        new NewsHttpRequestTask(this, my_url, this).execute();
    }

    @Override
    public void onTaskDone(String responseData) {
        // disabling the progress bar
        mProgressBar.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray articlesJsonArray = jsonObject.getJSONArray("articles");
            for (int i = 0; i < articlesJsonArray.length(); i++) {
                // accessing each article object in the JSONArray
                JSONObject article = articlesJsonArray.getJSONObject(i);
                // initializing an empty ArticlesItem
                ArticlesItem currentArticle = new ArticlesItem();
                // storing values of the article object properties
                if (article.has("author"))
                    currentArticle.setAuthor(article.getString("author"));
                if (article.has("title"))
                    currentArticle.setTitle(article.getString("title"));
                if (article.has("description"))
                    currentArticle.setDescription(article.getString("description"));
                if (article.has("url"))
                    currentArticle.setUrl(article.getString("url"));
                if (article.has("urlToImage"))
                    currentArticle.setUrlToImage(article.getString("urlToImage"));
                if (article.has("publishedAt"))
                    currentArticle.setPublishedAt(article.getString("publishedAt"));
                if (article.has("content"))
                    currentArticle.setContent(article.getString("content"));

                // adding an article to the articles List
                mArticleList.add(currentArticle);
            }

            // setting the adapter
            mArticleAdapter = new ArticleAdapter(getApplicationContext(), mArticleList);
            mRecyclerView.setAdapter(mArticleAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {
    }
}