package unit;

import configuration.PostServiceMockTestConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.dto.FullPost;
import ru.yandex.dto.PostPreview;
import ru.yandex.mapper.DTOMapper;
import ru.yandex.model.Post;
import ru.yandex.repository.PostRepository;
import ru.yandex.service.CommentService;
import ru.yandex.service.PostService;
import ru.yandex.service.TagService;
import ru.yandex.util.PageResponse;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@SpringJUnitConfig(PostServiceMockTestConfiguration.class)
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DTOMapper<Post, FullPost> fullPostMapper;

    @Autowired
    private DTOMapper<Post, PostPreview> previewMapper;

    @Autowired
    @Qualifier("id")
    private long id;

    @Test
    public void deletePostTest() {
        postService.deletePost(id);
        Mockito.verify(postRepository, times(1)).delete(id);
    }

    @Test
    public void getFullPostTest() {
        Post mockedPost = Mockito.mock(Post.class);
        Mockito.when(postRepository.getById(id)).thenReturn(mockedPost);
        postService.getPost(id);
        Mockito.verify(postRepository, times(1)).getById(id);
        Mockito.verify(tagService, times(1)).getByPostId(id);
        Mockito.verify(mockedPost, times(1)).setComments(any());
        Mockito.verify(mockedPost, times(1)).setTags(any());
        Mockito.verify(commentService, times(1)).getByPostId(id);
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
        List<Long> ids = List.of(1L);
        List<Post> posts = new ArrayList<>();
        Post mockedPost = Mockito.mock(Post.class);
        Mockito.when(mockedPost.getId()).thenReturn(1L);
        posts.add(mockedPost);
        PageResponse mockedPageResponse = Mockito.mock(PageResponse.class);
        Mockito.when(mockedPageResponse.ids()).thenReturn(ids);
        Mockito.when(postRepository.get(any(Long.class), any(Long.class), any(String.class))).thenReturn(mockedPageResponse);
        Mockito.when(postRepository.getByIds(ids)).thenReturn(posts);
        postService.getPageResponse(0L, 0L, "");

        Mockito.verify(postRepository, times(1))
                .get(any(Long.class), any(Long.class), any(String.class));
        Mockito.verify(postRepository, times(1))
                .getByIds(ids);
        Mockito.verify(tagService, times(1))
                .getByPostId(mockedPost.getId());
        Mockito.verify(commentService, times(1))
                .getByPostId(mockedPost.getId());
        Mockito.verify(previewMapper, times(1)).map(mockedPost);
    }
}
