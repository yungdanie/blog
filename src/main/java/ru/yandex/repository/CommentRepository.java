package ru.yandex.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.model.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("update comment set text = :text where id = :id")
    @Modifying
    void updateText(long id, String text);
}
