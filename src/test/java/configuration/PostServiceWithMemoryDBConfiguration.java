package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.dto.PostPreview;
import ru.yandex.mapper.DTOMapper;
import ru.yandex.mapper.PreviewMapper;
import ru.yandex.model.Post;
import ru.yandex.repository.PostRepository;
import ru.yandex.rowmapper.PostRowMapper;

import java.util.Random;

@Configuration
@Import({PostServiceMockTestConfiguration.class, InMemoryDBConfiguration.class})
public class PostServiceWithMemoryDBConfiguration {

    @Bean
    public PostRowMapper postRowMapper() {
        return new PostRowMapper();
    }

    @Bean
    public DTOMapper<Post, PostPreview> previewMapper() {
        return new PreviewMapper();
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public PostRepository postRepository(
            JdbcTemplate jdbcTemplate,
            PostRowMapper postRowMapper,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        return new PostRepository(jdbcTemplate, postRowMapper, namedParameterJdbcTemplate);
    }
}