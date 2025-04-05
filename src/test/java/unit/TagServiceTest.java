package unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.repository.TagRepository;
import ru.yandex.service.TagService;

import java.util.Random;

@SpringJUnitConfig(TagServiceTest.TagServiceConfiguration.class)
public class TagServiceTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    @Qualifier("id")
    private long id;

    @Test
    public void getByPostId() {
        tagService.getByPostId(id);
        Mockito.verify(tagRepository, Mockito.times(1)).getByPostId(id);
    }

    @Test
    public void deleteByPostIdTest() {
        tagService.deleteByPostId(id);
        Mockito.verify(tagRepository, Mockito.times(1)).deleteByPostId(id);
    }

    @Test
    public void saveTest() {
        String notEmpty = "notEmpty";
        tagService.save(notEmpty, id);
        Mockito.verify(tagRepository, Mockito.times(1)).save(notEmpty, id);
    }

    @Test
    public void saveWithEmptyTest() {
        String empty = "";
        Assertions.assertThrows(IllegalArgumentException.class, () -> tagService.save(empty, id));
    }

    @Test
    public void saveWithNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> tagService.save(null, id));
    }

    @Configuration
    static class TagServiceConfiguration {

        @Bean
        public TagService getImageService(TagRepository imageRepository) {
            return new TagService(imageRepository);
        }

        @Bean
        public TagRepository getImageRepository() {
            return Mockito.mock(TagRepository.class);
        }

        @Bean(name = "id")
        public long random() {
            return new Random().nextLong();
        }
    }
}
