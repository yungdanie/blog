package ru.yandex.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.configuration.RandomIdConfiguration;
import ru.yandex.repository.ImageRepository;
import ru.yandex.service.ImageService;

@SpringBootTest(classes = {ImageService.class, RandomIdConfiguration.class})
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @MockitoBean
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
}
