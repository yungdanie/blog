package ru.yandex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.repository.ImageRepository;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] getPostImage(long postId) {
        return imageRepository.getPostImage(postId);
    }

    public void replacePostImage(long postId, byte[] postImage) {
        imageRepository.replacePostImage(postId, postImage);
    }
}
