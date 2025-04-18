package ru.yandex.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Table
public class Post {

    @Id
    private Long id;

    private String text;

    private String title;

    @MappedCollection(idColumn = "post_id")
    private Set<Tag> tags = new HashSet<>();

    @MappedCollection(idColumn = "post_id")
    private Set<Comment> comments  = new HashSet<>();

    private Long likesCount = 0L;

    public Post(Long id, String title,  String text, Long likesCount) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.likesCount = likesCount;
    }

    public Post(Long likesCount, String text, String title) {
        this.likesCount = likesCount;
        this.text = text;
        this.title = title;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
