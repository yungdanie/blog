package ru.yandex.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.configuration.RandomIdConfiguration;
import ru.yandex.model.Comment;
import ru.yandex.repository.CommentRepository;
import ru.yandex.service.CommentService;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {CommentService.class, RandomIdConfiguration.class})
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockitoBean
    private CommentRepository commentRepository;

    @Autowired
    @Qualifier("id")
    private Long id;

    @Test
    public void update() {
        String nonEmpty = "non-empty";
        commentService.update(id, nonEmpty);
        Mockito.verify(commentRepository, Mockito.times(1)).updateText(id, nonEmpty);
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
        Mockito.verify(commentRepository, Mockito.times(1))
                .save(any(Comment.class));
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

}
