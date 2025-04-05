package ru.yandex.model;

import java.util.List;

public class Post {

    private Long id;

    private String text;

    private String title;

    private List<Tag> tags;

    private List<Comment> comments;

    private Long likesCount;

    public Post(Long id, String title,  String text, Long likesCount) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.likesCount = likesCount;
    }

    public Post() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
