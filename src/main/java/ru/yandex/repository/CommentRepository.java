package ru.yandex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.model.Comment;
import ru.yandex.rowmapper.CommentRowMapper;

import java.util.List;

@Repository
public class CommentRepository {

    private static final String DELETE_QUERY = """
            delete from comment where id = ?;
            """;

    private static final String SAVE_QUERY = """
            insert into comment(post_id, text) values (?, ?);
            """;

    private static final String GET_BY_POST_ID_QUERY = """
            select comment.id as comment_id, comment.text as comment_text, comment.post_id as comment_post_id
            from comment
            where comment.post_id = ?;
            """;

    private static final String UPDATE_QUERY = """
            update comment set text = ? where id = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    private final CommentRowMapper commentRowMapper;

    @Autowired
    public CommentRepository(JdbcTemplate jdbcTemplate, CommentRowMapper commentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.commentRowMapper = commentRowMapper;
    }

    public List<Comment> getByPostId(long postId) {
        return jdbcTemplate.query(GET_BY_POST_ID_QUERY, commentRowMapper, postId);
    }

    public void save(long postId, String text) {
        jdbcTemplate.update(SAVE_QUERY, postId, text);
    }

    public void delete(long commentId) {
        jdbcTemplate.update(DELETE_QUERY, commentId);
    }

    public void update(long commentId, String text) {
        jdbcTemplate.update(UPDATE_QUERY, text, commentId);
    }
}
