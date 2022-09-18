package com.moengage.newsapp.modal;

import java.util.List;

public class ModalNews {
    private List<ArticlesItem> articles;
    private String status;

    public void setArticles(List<ArticlesItem> articles) {
        this.articles = articles;
    }

    public List<ArticlesItem> getArticles() {
        return articles;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "ModalNews{" +
                        "articles = '" + articles + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}