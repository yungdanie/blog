package ru.yandex.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {

    List<Post> findAllByOrderById(Pageable pageable);

    @Query("""
        SELECT p.* FROM post p
        JOIN tag t ON p.id = t.post_id
        WHERE t.name ilike :tagName
        ORDER BY p.id
        limit :limit
        offset :offset
    """)
    List<Post> findAllByTagNameOrderById(
            @Param("tagName") String tagName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    @Modifying
    @Query("update post p set likes_count = (likes_count + 1) where p.id = :postId")
    void addLike(long postId);

    @Modifying
    @Query("update post p set likes_count = (likes_count - 1) where p.id = :postId")
    void removeLike(long postId);
}
