package com.moengage.newsapp.interfaces;

public interface OnTaskDoneListener {
    void onTaskDone(String responseData);

    void onError();
}