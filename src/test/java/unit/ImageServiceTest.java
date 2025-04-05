package unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.repository.ImageRepository;
import ru.yandex.service.ImageService;

import java.util.Random;

@SpringJUnitConfig(ImageServiceTest.ImageServiceConfiguration.class)
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    @Qualifier("id")
    private Long id;

    @Test
    public void getPostImageTest() {
        imageService.getPostImage(id);
        Mockito.verify(imageRepository, Mockito.times(1)).getPostImage(id);
    }

    @Test
    public void replacePostImage() {
        var bytes = new byte[] {};
        imageService.replacePostImage(id, bytes);
        Mockito.verify(imageRepository, Mockito.times(1)).replacePostImage(id, bytes);
    }

    @Configuration
    static class ImageServiceConfiguration {

        @Bean
        public ImageService getImageService(ImageRepository imageRepository) {
            return new ImageService(imageRepository);
        }

        @Bean
        public ImageRepository getImageRepository() {
            return Mockito.mock(ImageRepository.class);
        }

        @Bean(name = "id")
        public Long random() {
            return new Random().nextLong();
        }

    }
}
