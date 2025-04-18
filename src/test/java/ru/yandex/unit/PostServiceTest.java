package ru.yandex.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.configuration.RandomIdConfiguration;
import ru.yandex.dto.FullPost;
import ru.yandex.dto.PostPreview;
import ru.yandex.mapper.DTOMapper;
import ru.yandex.model.Post;
import ru.yandex.repository.PostRepository;
import ru.yandex.service.ImageService;
import ru.yandex.service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = {PostService.class, RandomIdConfiguration.class})
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockitoBean
    private PostRepository postRepository;

    @MockitoBean
    private ImageService imageService;

    @MockitoBean
    private DTOMapper<Post, FullPost> fullPostMapper;

    @MockitoBean
    private DTOMapper<Post, PostPreview> previewMapper;

    @Autowired
    @Qualifier("id")
    private long id;

    @Test
    public void deletePostTest() {
        postService.deletePost(id);
        Mockito.verify(postRepository, times(1)).deleteById(id);
    }

    @Test
    public void getFullPostTest() {
        Post mockedPost = Mockito.mock(Post.class);
        Mockito.when(postRepository.findById(id)).thenReturn(Optional.of(mockedPost));
        postService.getPost(id);
        Mockito.verify(postRepository, times(1)).findById(id);
        Mockito.verify(fullPostMapper, times(1)).map(mockedPost);
    }

    @Test
    public void likeTest() {
        postService.like(id, true);
        Mockito.verify(postRepository, times(1)).addLike(id);
        postService.like(id, false);
        Mockito.verify(postRepository, times(1)).removeLike(id);
    }

    @Test
    public void paginateTest() {
        List<Post> posts = new ArrayList<>();
        Post mockedPost = Mockito.mock(Post.class);
        Mockito.when(mockedPost.getId()).thenReturn(1L);
        posts.add(mockedPost);

        Mockito.when(postRepository.findAllByOrderById(any()))
                .thenReturn(posts);

        postService.getPageResponse(0, 0, "");

        Mockito.verify(postRepository, times(1))
                .findAllByOrderById(any());
        Mockito.verify(previewMapper, times(1)).map(mockedPost);
    }
}
