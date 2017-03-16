package com.comeonbaby.android.app.requests.models;

/**
 * Created by Home on 28.02.2017.
 */

public class QARequest {
    private Long id;
    private String operation;
    private String user;
    private String title;
    private String text;
    private Boolean is_access;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Boolean getIs_access() {
        return is_access;
    }

    public void setIs_access(Boolean is_access) {
        this.is_access = is_access;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "QARequest{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", user='" + user + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", is_access=" + is_access +
                '}';
    }
}
