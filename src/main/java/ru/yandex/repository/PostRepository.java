package ru.yandex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.yandex.dto.PostEdit;
import ru.yandex.model.Post;
import ru.yandex.rowmapper.PostRowMapper;
import ru.yandex.util.PageResponse;

import java.util.*;

@Repository
public class PostRepository {

    private static final String WHERE_SEARCH_QUERY = "left join tag on tag.post_id = post.id where tag.name ilike CONCAT('%', ?, '%')";

    private static final String ADD_LIKE_QUERY = "update post set likes_count = (post.likes_count + 1) where post.id = ?";

    private static final String REMOVE_LIKE_QUERY = "update post set likes_count = (post.likes_count - 1) where post.id = ?";

    private static final String DELETE_QUERY = "delete from post where post.id = ?";

    private static final String GET_BY_ID_QUERY = """
            select post.id as post_id, post.title as post_title, post.text as post_text, post.likes_count as post_likes_count
            from post
            where post.id in (:ids);
            """;

    private final static String UPDATE_QUERY = """
            update post set title = ?, text = ? where post.id = ?;
            """;

    private final static String PAGING_QUERY = """
            select post.id
            from post
            %s
            group by post.id
            order by post.id
            limit ?
            offset ?
            """;

    private final JdbcTemplate jdbcTemplate;

    private final PostRowMapper postRowMapper;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PostRepository(
            JdbcTemplate jdbcTemplate,
            PostRowMapper postRowMapper,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.postRowMapper = postRowMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void delete(long postId) {
        jdbcTemplate.update(DELETE_QUERY, postId);
    }

    public void update(PostEdit post) {
        jdbcTemplate.update(UPDATE_QUERY, post.getTitle(), post.getText(), post.getId());
    }

    public Long save(PostEdit post) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("post")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("title", post.getTitle());
        params.put("text", post.getText());
        params.put("likes_count", 0);

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Post getById(long id) {
        return namedParameterJdbcTemplate.queryForObject(GET_BY_ID_QUERY, Map.of("ids", id), postRowMapper);
    }

    public List<Post> getByIds(List<Long> ids) {
        return namedParameterJdbcTemplate.query(GET_BY_ID_QUERY, Map.of("ids", ids), postRowMapper);
    }

    public PageResponse get(long pageSize, long pageNumber, @Nullable String search) {
        List<Long> postIds = new ArrayList<>();
        long offset = getOffset(pageSize, pageNumber);
        pageSize = pageSize + 1;
        Object[] params = Objects.nonNull(search) && !search.isEmpty()
                ? new Object[] {search.toLowerCase(), pageSize, offset} : new Object[] {pageSize, offset};

        try {
            postIds = jdbcTemplate.queryForList(
                    buildQuery(search),
                    Long.class,
                    params
            );
        } catch (EmptyResultDataAccessException ignored) {}

        boolean hasNext = postIds.size() == pageSize;

        if (hasNext) {
            postIds.removeLast();
        }

        return new PageResponse(postIds, hasNext);
    }

    public void addLike(long postId) {
        jdbcTemplate.update(ADD_LIKE_QUERY, postId);
    }

    public void removeLike(long postId) {
        jdbcTemplate.update(REMOVE_LIKE_QUERY, postId);
    }

    private long getOffset(long pageSize, long pageNumber) {
        return (pageNumber - 1) * pageSize;
    }

    private String buildQuery(@Nullable String search) {
        return String.format(
                PAGING_QUERY,
                Objects.nonNull(search) && !search.isEmpty() ? WHERE_SEARCH_QUERY : ""
        );
    }
}
