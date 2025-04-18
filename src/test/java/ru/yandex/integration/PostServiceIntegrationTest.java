package ru.yandex.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.BlogApplication;
import ru.yandex.dto.PostEdit;
import ru.yandex.service.CommentService;
import ru.yandex.service.ImageService;
import ru.yandex.service.PostService;
import ru.yandex.util.PostPageResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@Transactional
public class PostServiceIntegrationTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected PostService postService;

    @Autowired
    protected CommentService commentService;

    @MockitoBean
    protected ImageService imageService;

    protected Long counter = 0L;

    protected final MultipartFile mockedFile = Mockito.mock(MultipartFile.class);

    @BeforeEach
    protected void configurePageRequest() throws IOException {
        Mockito.when(mockedFile.getBytes()).thenReturn(new byte[] {});
        Mockito.doNothing().when(imageService).replacePostImage(any(Long.class), any(byte[].class));
    }

    @Test
    public void firstPageRequestTest() {
        PostPageResponse postPageResponse = postService.getPageResponse(null, null, null);
        Assertions.assertNotNull(postPageResponse);
        Assertions.assertNotNull(postPageResponse.postPreviews());
        Assertions.assertNotNull(postPageResponse.pageable());

        Assertions.assertTrue(postPageResponse.postPreviews().isEmpty());

        Assertions.assertEquals(0L, postPageResponse.pageable().pageNumber());
        Assertions.assertEquals(10L, postPageResponse.pageable().pageSize());
    }

    @Test
    public void firstNonEmptyPageRequestTest() {
        Long id = save();

        Assertions.assertNotNull(id);

        PostPageResponse postPageResponse = postService.getPageResponse(null, null, null);

        Assertions.assertNotNull(postPageResponse);
        Assertions.assertNotNull(postPageResponse.postPreviews());
        Assertions.assertNotNull(postPageResponse.pageable());

        Assertions.assertFalse(postPageResponse.postPreviews().isEmpty());
        Assertions.assertFalse(postPageResponse.pageable().hasNext());
        Assertions.assertEquals(0L, postPageResponse.pageable().pageNumber());
        Assertions.assertEquals(10L, postPageResponse.pageable().pageSize());
    }

    @Test
    public void fewPostsPageRequestTest() {
        Long firstId = save();
        Long secondId = save();

        Assertions.assertNotNull(firstId);
        Assertions.assertNotNull(secondId);

        PostPageResponse postPageResponse = postService.getPageResponse(null, null, null);

        Assertions.assertNotNull(postPageResponse);
        Assertions.assertNotNull(postPageResponse.postPreviews());
        Assertions.assertNotNull(postPageResponse.pageable());

        Assertions.assertFalse(postPageResponse.postPreviews().isEmpty());
        Assertions.assertFalse(postPageResponse.pageable().hasNext());
        Assertions.assertEquals(0L, postPageResponse.pageable().pageNumber());
        Assertions.assertEquals(10L, postPageResponse.pageable().pageSize());

        Assertions.assertTrue(postPageResponse.postPreviews().stream().anyMatch(post -> post.id().equals(firstId)));
        Assertions.assertTrue(postPageResponse.postPreviews().stream().anyMatch(post -> post.id().equals(secondId)));
    }

    @Test
    public void singlePageSizeTest() {
        Long firstId = save();
        Long secondId = save();

        Assertions.assertNotNull(firstId);
        Assertions.assertNotNull(secondId);

        PostPageResponse postPageResponse = postService.getPageResponse(1, 0, null);

        Assertions.assertNotNull(postPageResponse);
        Assertions.assertNotNull(postPageResponse.postPreviews());
        Assertions.assertNotNull(postPageResponse.pageable());

        Assertions.assertFalse(postPageResponse.postPreviews().isEmpty());
        Assertions.assertTrue(postPageResponse.pageable().hasNext());
        Assertions.assertEquals(0L, postPageResponse.pageable().pageNumber());
        Assertions.assertEquals(1L, postPageResponse.pageable().pageSize());

        Assertions.assertEquals(1, postPageResponse.postPreviews().size());
        Assertions.assertEquals(postPageResponse.postPreviews().getFirst().id(), firstId);
    }

    protected Long save() {
        PostEdit postEdit = new PostEdit(counter++, "text", "title", "tags tags");
        return postService.save(postEdit, mockedFile);
    }
}
