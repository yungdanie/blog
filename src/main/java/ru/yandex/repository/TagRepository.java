package ru.yandex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.rowmapper.TagRowMapper;
import ru.yandex.model.Tag;

import java.util.List;
import java.util.Objects;

@Repository
public class TagRepository {

    private static final String GET_BY_POST_ID_QUERY = """
            select tag.id as tag_id, tag.name as tag_name, tag.post_id as tag_post_id from tag
            where tag.post_id = ?
            """;

    private static final String SAVE_QUERY = """
            insert into tag (name, post_id) values (?, ?)
            """;

    private static final String DELETE_QUERY = """
            delete from tag where post_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    private final TagRowMapper tagRowMapper;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate, TagRowMapper tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    public List<Tag> getByPostId(Long postId) {
        return jdbcTemplate.query(GET_BY_POST_ID_QUERY, tagRowMapper, postId);
    }

    public void deleteByPostId(Long postId) {
        jdbcTemplate.update(DELETE_QUERY, postId);
    }

    public void save(String name, Long postId) {
        Objects.requireNonNull(postId);
        jdbcTemplate.update(SAVE_QUERY, name, postId);
    }
}
