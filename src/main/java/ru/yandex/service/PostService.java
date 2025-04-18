package ru.yandex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.dto.FullPost;
import ru.yandex.dto.PostEdit;
import ru.yandex.dto.PostPreview;
import ru.yandex.exception.ImageUpdateError;
import ru.yandex.mapper.DTOMapper;
import ru.yandex.model.Post;
import ru.yandex.model.Tag;
import ru.yandex.repository.PostRepository;
import ru.yandex.util.PostPageResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    private final DTOMapper<Post, PostPreview> previewMapper;

    private final DTOMapper<Post, FullPost> fullPostMapper;

    private final ImageService imageService;

    private final PostRepository postRepository;

    @Autowired
    public PostService(
            DTOMapper<Post, PostPreview> previewMapper,
            DTOMapper<Post, FullPost> fullPostMapper,
            ImageService imageService,
            PostRepository postRepository
    ) {
        this.previewMapper = previewMapper;
        this.fullPostMapper = fullPostMapper;
        this.imageService = imageService;
        this.postRepository = postRepository;
    }

    public void deletePost(long postId) {
        this.postRepository.deleteById(postId);
    }

    public FullPost getPost(long postId) {
        return fullPostMapper.map(postRepository.findById(postId).orElseThrow());
    }

    public PostPageResponse getPageResponse(Integer pageSize, Integer pageNumber, @Nullable String search) {
        pageNumber = getPageNumberOrDefault(pageNumber);
        pageSize = getPageSizeOrDefault(pageSize);

        Pageable pageable = PageRequest.of(pageNumber, pageSize + 1);
        List<Post> posts = Objects.isNull(search) || search.isEmpty() ?
                postRepository.findAllByOrderById(pageable) :
                postRepository.findAllByTagNameOrderById(search, pageNumber * pageSize, pageSize);

        boolean hasNext = posts.size() == (pageSize + 1);

        if (hasNext) {
            posts = posts.stream().sorted(Comparator.comparing(Post::getId)).collect(toList());
            posts.removeLast();
        }

        return new PostPageResponse(
                posts.stream()
                        .map(previewMapper::map)
                        .collect(toList()),
                new ru.yandex.util.Pageable(pageNumber, pageSize, hasNext)
        );
    }

    public Long update(PostEdit postEdit, MultipartFile image) {
        Objects.requireNonNull(postEdit.getId());

        Post post = postRepository.findById(postEdit.getId()).orElseThrow();

        post.setText(postEdit.getText());
        post.setTitle(postEdit.getTitle());

        try {
            imageService.replacePostImage(postEdit.getId(), image.getBytes());
        } catch (IOException e) {
            throw new ImageUpdateError();
        }

        post.setTags(
                Arrays.stream(postEdit.getTags().split(" "))
                        .map(Tag::new)
                        .collect(Collectors.toSet())
        );

        postRepository.save(post);

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

        Post newPost = new Post();

        newPost.setTitle(post.getTitle());
        newPost.setText(post.getText());

        newPost.setTags(
                Arrays.stream(post.getTags().split(" "))
                        .map(Tag::new)
                        .collect(Collectors.toSet())
        );

        postRepository.save(newPost);

        try {
            imageService.replacePostImage(newPost.getId(), image.getBytes());
        } catch (IOException e) {
            throw new ImageUpdateError();
        }

        return newPost.getId();
    }

    private int getPageNumberOrDefault(Integer pageNumber) {
        return Objects.isNull(pageNumber) || pageNumber < 0 ? 0 : pageNumber;
    }

    private int getPageSizeOrDefault(Integer pageSize) {
        return Objects.isNull(pageSize) || pageSize <= 0 ? 10 : pageSize;
    }
}
