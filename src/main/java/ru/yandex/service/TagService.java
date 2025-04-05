package ru.yandex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.model.Tag;
import ru.yandex.repository.TagRepository;

import java.util.List;
import java.util.Objects;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getByPostId(long postId) {
        return tagRepository.getByPostId(postId);
    }

    public void deleteByPostId(long postId) {
        tagRepository.deleteByPostId(postId);
    }

    public void save(String tagName, long postId) {
        if (Objects.isNull(tagName) || tagName.isEmpty()) {
            throw new IllegalArgumentException();
        }

        tagRepository.save(tagName, postId);
    }
}
