package ru.yandex.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
                rs.getLong("post_id"),
                rs.getString("post_title"),
                rs.getString("post_text"),
                rs.getLong("post_likes_count")
        );
    }
}

