package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.controller.PostController;
import ru.yandex.dto.CommentDTO;
import ru.yandex.dto.FullPost;
import ru.yandex.mapper.CommentMapper;
import ru.yandex.mapper.DTOMapper;
import ru.yandex.mapper.FullPostMapper;
import ru.yandex.model.Comment;
import ru.yandex.model.Post;
import ru.yandex.service.PostService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@Configuration
@Import({PostServiceWithMemoryDBConfiguration.class, ThymeleafConfiguration.class})
public class PostControllerWebApplicationConfiguration {

    @Bean
    public PostController postController(PostService postService) {
        return new PostController(postService);
    }

    @Bean
    public DTOMapper<Comment, CommentDTO> commentMapper() {
        return new CommentMapper();
    }

    @Bean
    public DTOMapper<Post, FullPost> fullPostMapper(DTOMapper<Comment, CommentDTO> commentMapper) {
        return new FullPostMapper(commentMapper);
    }

    @Bean
    public DocumentBuilder documentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        return factory.newDocumentBuilder();
    }

}
