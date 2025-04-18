package ru.yandex.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.configuration.DBConfiguration;
import ru.yandex.model.Post;
import ru.yandex.repository.PostRepository;

import java.util.HashSet;
import java.util.Set;

@DataJdbcTest
@PropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(DBConfiguration.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void getAllTest() {
        Set<Long> posts = new HashSet<>(100);

        for (int i = 0; i < 100; i++) {
            var post = postRepository.save(new Post(0L, "", ""));
            posts.add(post.getId());
        }

        Set<Long> dbPosts = new HashSet<>(100);
        postRepository.findAll().forEach(post -> dbPosts.add(post.getId()));

        Assertions.assertTrue(posts.containsAll(dbPosts));
    }

    @Test
    public void findByIdTest() {
        var post = postRepository.save(new Post(0L, "", ""));
        var dbPost = postRepository.findById(post.getId()).orElseThrow();
        Assertions.assertEquals(post.getId(), dbPost.getId());
    }

    @Test
    public void addLikeTest() {
        var post = postRepository.save(new Post(0L, "", ""));
        postRepository.addLike(post.getId());

        Assertions.assertEquals(
                post.getLikesCount() + 1,
                postRepository.findById(post.getId()).orElseThrow().getLikesCount()
        );
    }


}
