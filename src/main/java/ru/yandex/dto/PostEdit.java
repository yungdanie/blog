package ru.yandex.dto;

public class PostEdit {

    private Long id;

    private String text;

    private String title;

    private String tags;

    public PostEdit(Long id, String text, String title, String tags) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.tags = tags;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
