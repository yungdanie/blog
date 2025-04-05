package ru.yandex.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.dto.CommentDTO;
import ru.yandex.model.Comment;

@Component
public class CommentMapper implements DTOMapper<Comment, CommentDTO> {

    @Override
    public CommentDTO map(Comment from) {
        return new CommentDTO(
                from.getId(),
                from.getText()
        );
    }
}
