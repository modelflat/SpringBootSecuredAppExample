package com.github.modelflat.SpringBootSecuredAppExample.model;

public class Post {

    private String byUser;
    private String body;

    public Post() {}

    public Post(String byUser, String body) {
        this.byUser = byUser;
        this.body = body;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
