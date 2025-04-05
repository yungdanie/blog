package unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.repository.CommentRepository;
import ru.yandex.service.CommentService;

import java.util.Random;

@SpringJUnitConfig(CommentServiceTest.CommentServiceConfiguration.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    @Qualifier("id")
    private Long id;

    @Test
    public void getByPostIdTest() {
        commentService.getByPostId(id);
        Mockito.verify(commentRepository, Mockito.times(1)).getByPostId(id);
    }

    @Test
    public void update() {
        String nonEmpty = "non-empty";
        commentService.update(id, nonEmpty);
        Mockito.verify(commentRepository, Mockito.times(1)).update(id, nonEmpty);
    }

    @Test
    public void updateWithEmptyText() {
        String empty = "";
        Assertions.assertThrows(IllegalArgumentException.class, () -> commentService.update(id, empty));
    }

    @Test
    public void updateWithNullText() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> commentService.update(id, null));
    }

    @Test
    public void save() {
        String nonEmpty = "non-empty";
        commentService.save(id, nonEmpty);
        Mockito.verify(commentRepository, Mockito.times(1)).save(id, nonEmpty);
    }

    @Test
    public void saveWithEmptyText() {
        String empty = "";
        Assertions.assertThrows(IllegalArgumentException.class, () -> commentService.save(id, empty));
    }

    @Test
    public void saveWithNullText() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> commentService.save(id, null));
    }


    @Configuration
    static class CommentServiceConfiguration {

        @Bean
        public CommentService commentService(CommentRepository commentRepository) {
            return new CommentService(commentRepository);
        }

        @Bean
        public CommentRepository commentRepository() {
            return Mockito.mock(CommentRepository.class);
        }

        @Bean(name = "id")
        public Long random() {
            return new Random().nextLong();
        }
    }
}
