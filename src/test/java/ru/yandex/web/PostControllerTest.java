package ru.yandex.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.configuration.PostControllerWebApplicationConfiguration;
import ru.yandex.controller.PostController;
import ru.yandex.dto.FullPost;
import ru.yandex.dto.PostPreview;
import ru.yandex.service.PostService;
import ru.yandex.util.Pageable;
import ru.yandex.util.PostPageResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@Import(PostControllerWebApplicationConfiguration.class)
public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postPage() throws Exception {
        String title = "title";
        FullPost post = new FullPost(0L, title, "text", List.of(), List.of(), 0);
        Mockito.when(postService.getPost(any(Long.class))).thenReturn(post);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/posts/%s", any(Long.class))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//body/table/tr[2]/td/h2").string(title));
    }

    @Test
    public void mainPage() throws Exception {
        String title = "title";
        PostPreview firstPost = new PostPreview(0L, title, "text", 0, 0, List.of());
        PostPreview secondPost = new PostPreview(1L, title, "text", 0, 0, List.of());

        Mockito.when(postService.getPageResponse(any(), any(), any()))
                .thenReturn(new PostPageResponse(List.of(firstPost, secondPost), new Pageable(0, 0, false)));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/posts")
                                .param("pageNumber", String.valueOf(0))
                                .param("pageSize", String.valueOf(10L))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(xpath("//body/table/tr[2]/td/a/h2").string("title"))
                .andExpect(xpath("//body/table/tr[3]/td/a/h2").string("title"));
    }

}
