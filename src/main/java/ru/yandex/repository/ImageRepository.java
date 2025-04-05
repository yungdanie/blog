package ru.yandex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepository {

    private static final String GET_POST_IMAGE = """
            select image.data from image where post_id = ?
            """;

    private static final String DELETE_IMAGE_QUERY = """
            delete from image where post_id = ?
            """;

    private static final String INSERT_IMAGE = """
            insert into image (data, post_id) values (?, ?)
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ImageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public byte[] getPostImage(long postId) {
        return jdbcTemplate.queryForObject(GET_POST_IMAGE, byte[].class, postId);
    }

    public void replacePostImage(long postId, byte[] postImage) {
        jdbcTemplate.update(DELETE_IMAGE_QUERY, postId);
        jdbcTemplate.update(INSERT_IMAGE, postImage, postId);
    }
}
