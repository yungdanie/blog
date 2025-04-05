package ru.yandex.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.dto.CommentDTO;
import ru.yandex.dto.FullPost;
import ru.yandex.model.Comment;
import ru.yandex.model.Post;
import ru.yandex.model.Tag;

@Component
public class FullPostMapper implements DTOMapper<Post, FullPost> {

    private final DTOMapper<Comment, CommentDTO> commentMapper;

    @Autowired
    public FullPostMapper(DTOMapper<Comment, CommentDTO> commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public FullPost map(Post from) {
        return new FullPost(
                from.getId(),
                from.getTitle(),
                from.getText(),
                from.getTags().stream()
                        .map(Tag::getName)
                        .toList(),
                from.getComments().stream()
                        .map(commentMapper::map)
                        .toList(),
                from.getLikesCount()
        );
    }
}
