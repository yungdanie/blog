package ru.yandex.service;

import org.springframework.stereotype.Service;
import ru.yandex.model.Comment;
import ru.yandex.repository.CommentRepository;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getByPostId(long postId) {
        return commentRepository.getByPostId(postId);
    }

    public void update(long commentId, String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }

        commentRepository.update(commentId, text);
    }

    public void save(long postId, String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }

        commentRepository.save(postId, text);
    }

    public void delete(long commentId) {
        commentRepository.delete(commentId);
    }
}
