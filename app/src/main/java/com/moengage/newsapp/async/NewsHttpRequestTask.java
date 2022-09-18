package com.moengage.newsapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.moengage.newsapp.interfaces.OnTaskDoneListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsHttpRequestTask extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private OnTaskDoneListener onTaskDoneListener;
    private String urlStr = "";

    public NewsHttpRequestTask(Context context, String url, OnTaskDoneListener onTaskDoneListener) {
        this.mContext = context;
        this.urlStr = url;
        this.onTaskDoneListener = onTaskDoneListener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                // setting the  Request Method Type
                httpURLConnection.setRequestMethod("GET");
                // adding the headers for request
                httpURLConnection.setRequestProperty("Content-length", "0");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setConnectTimeout(100000);
                httpURLConnection.setReadTimeout(100000);

                httpURLConnection.connect();

                int responseCode = httpURLConnection.getResponseCode();
                // to log the response code of your request
                Log.d(NewsHttpRequestTask.class.getSimpleName(), "MyHttpRequestTask doInBackground::" + httpURLConnection.getResponseCode());
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // this is done so that there are no open connections left when this task is going to complete
                httpURLConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (onTaskDoneListener != null)
            if (s != null && !s.isEmpty())
                onTaskDoneListener.onTaskDone(s);
            else
                onTaskDoneListener.onError();
    }
}