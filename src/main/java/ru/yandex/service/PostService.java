package ru.yandex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.dto.FullPost;
import ru.yandex.dto.PostEdit;
import ru.yandex.dto.PostPreview;
import ru.yandex.exception.ImageUpdateError;
import ru.yandex.mapper.DTOMapper;
import ru.yandex.model.Post;
import ru.yandex.repository.PostRepository;
import ru.yandex.util.PageResponse;
import ru.yandex.util.Pageable;
import ru.yandex.util.PostPageResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final TagService tagService;

    private final DTOMapper<Post, PostPreview> previewMapper;

    private final DTOMapper<Post, FullPost> fullPostMapper;

    private final CommentService commentService;

    private final ImageService imageService;

    @Autowired
    public PostService(
            PostRepository postRepository,
            TagService tagService,
            DTOMapper<Post, PostPreview> previewMapper,
            DTOMapper<Post, FullPost> fullPostMapper,
            CommentService commentService,
            ImageService imageService
    ) {
        this.postRepository = postRepository;
        this.tagService = tagService;
        this.previewMapper = previewMapper;
        this.fullPostMapper = fullPostMapper;
        this.commentService = commentService;
        this.imageService = imageService;
    }

    public void deletePost(long postId) {
        this.postRepository.delete(postId);
    }

    public FullPost getPost(long postId) {
        var post = postRepository.getById(postId);
        post.setTags(tagService.getByPostId(postId));
        post.setComments(commentService.getByPostId(postId));
        return fullPostMapper.map(post);
    }

    public PostPageResponse getPageResponse(Long pageSize, Long pageNumber, @Nullable String search) {
        pageNumber = getPageNumberOrDefault(pageNumber);
        pageSize = getPageSizeOrDefault(pageSize);
        PageResponse pageResponse = postRepository.get(pageSize, pageNumber, search);
        List<Post> posts = new ArrayList<>();

        if (!pageResponse.ids().isEmpty()) {
            posts = postRepository.getByIds(pageResponse.ids());
        }

        posts.forEach(post -> {
            post.setTags(tagService.getByPostId(post.getId()));
            post.setComments(commentService.getByPostId(post.getId()));
        });

        return new PostPageResponse(
                posts.stream().map(previewMapper::map).toList(),
                new Pageable(pageNumber, pageSize, pageResponse.hasNext())
        );
    }

    public Long update(PostEdit postEdit, MultipartFile image) {
        Objects.requireNonNull(postEdit.getId());

        postRepository.update(postEdit);

        try {
            imageService.replacePostImage(postEdit.getId(), image.getBytes());
        } catch (IOException e) {
            throw new ImageUpdateError();
        }

        tagService.deleteByPostId(postEdit.getId());
        Arrays.stream(postEdit.getTags().split(" "))
                .forEach(tagName -> tagService.save(tagName, postEdit.getId()));

        return postEdit.getId();
    }

    public void like(long postId, boolean like) {
        if (like) {
            addLike(postId);
        } else {
            removeLike(postId);
        }
    }

    public void addLike(long postId) {
        postRepository.addLike(postId);
    }

    public void removeLike(long postId) {
        postRepository.removeLike(postId);
    }

    public Long save(PostEdit post, MultipartFile image) {
        if (Objects.isNull(post) || Objects.isNull(image)) {
            throw new IllegalArgumentException();
        }

        Long postId = postRepository.save(post);

        if (Objects.isNull(postId)) {
            throw new RuntimeException("Error saving post");
        }

        try {
            imageService.replacePostImage(postId, image.getBytes());
        } catch (IOException e) {
            throw new ImageUpdateError();
        }

        Arrays.stream(post.getTags().split(" ")).forEach(tagName -> tagService.save(tagName, postId));

        return postId;
    }

    private long getPageNumberOrDefault(Long pageNumber) {
        return Objects.isNull(pageNumber) || pageNumber < 0 ? 1 : pageNumber;
    }

    private long getPageSizeOrDefault(Long pageSize) {
        return Objects.isNull(pageSize) || pageSize < 0 ? 10 : pageSize;
    }
}
