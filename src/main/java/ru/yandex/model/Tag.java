package ru.yandex.model;

public class Tag {

    private Long id;

    private String name;

    private Long postId;

    public Tag(Long id, String name, Long postId) {
        this.id = id;
        this.name = name;
        this.postId = postId;
    }

    public Tag() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
