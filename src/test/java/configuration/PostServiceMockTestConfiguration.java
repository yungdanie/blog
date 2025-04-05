package configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.dto.FullPost;
import ru.yandex.dto.PostPreview;
import ru.yandex.mapper.DTOMapper;
import ru.yandex.mapper.FullPostMapper;
import ru.yandex.mapper.PreviewMapper;
import ru.yandex.model.Post;
import ru.yandex.repository.PostRepository;
import ru.yandex.service.CommentService;
import ru.yandex.service.ImageService;
import ru.yandex.service.PostService;
import ru.yandex.service.TagService;

import java.util.Random;

@Configuration
public class PostServiceMockTestConfiguration {

    @Bean
    public PostService postService(
            PostRepository postRepository,
            TagService tagService,
            DTOMapper<Post, PostPreview> previewMapper,
            DTOMapper<Post, FullPost> fullPostMapper,
            CommentService commentService,
            ImageService imageService
    ) {
        return new PostService(
                postRepository,
                tagService,
                previewMapper,
                fullPostMapper,
                commentService,
                imageService
        );
    }

    @Bean
    public DTOMapper<Post, PostPreview> previewMapper() {
        return Mockito.mock(PreviewMapper.class);
    }

    @Bean
    public DTOMapper<Post, FullPost> fullPostMapper() {
        return Mockito.mock(FullPostMapper.class);
    }

    @Bean
    public ImageService imageService() {
        return Mockito.mock(ImageService.class);
    }

    @Bean
    public PostRepository postRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    public TagService tagService() {
        return Mockito.mock(TagService.class);
    }

    @Bean
    public CommentService commentService() {
        return Mockito.mock(CommentService.class);
    }

    @Bean(name = "id")
    public Long random() {
        return new Random().nextLong();
    }
}
