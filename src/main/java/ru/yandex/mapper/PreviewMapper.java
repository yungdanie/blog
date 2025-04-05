package ru.yandex.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.dto.PostPreview;
import ru.yandex.model.Post;
import ru.yandex.model.Tag;

@Component
public class PreviewMapper implements DTOMapper<Post, PostPreview> {

    @Override
    public PostPreview map(Post from) {
        return new PostPreview(
                from.getId(),
                from.getTitle(),
                from.getText().split(System.lineSeparator())[0],
                from.getLikesCount(),
                from.getComments().size(),
                from.getTags().stream()
                        .map(Tag::getName)
                        .toList()
        );
    }
}
