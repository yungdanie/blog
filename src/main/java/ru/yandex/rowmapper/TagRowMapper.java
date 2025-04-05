package ru.yandex.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                rs.getLong("tag_id"),
                rs.getString("tag_name"),
                rs.getLong("tag_post_id")
        );
    }
}
