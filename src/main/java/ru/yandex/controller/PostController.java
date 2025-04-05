package ru.yandex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.yandex.dto.PostEdit;
import ru.yandex.service.PostService;
import ru.yandex.util.PostPageResponse;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String getPosts(
            @RequestParam(name = "pageNumber", required = false) Long pageNumber,
            @RequestParam(name = "pageSize", required = false) Long pageSize,
            @RequestParam(name = "search", required = false) String search,
            Model model
    ) {
        PostPageResponse postPageResponse = postService.getPageResponse(pageSize, pageNumber, search);
        model.addAttribute("posts", postPageResponse.postPreviews());
        model.addAttribute("paging", postPageResponse.pageable());
        model.addAttribute("search", search);
        return "posts";
    }

    @GetMapping("/{postId}")
    public String getPost(@PathVariable("postId") long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "post";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @GetMapping("/add")
    public String addPost() {
        return "add-post";
    }

    @GetMapping("/{postId}/edit")
    public String editPost(@PathVariable Long postId, Model model) {
        model.addAttribute("post", postService.getPost(postId));
        return "add-post";
    }


    @PostMapping("/save")
    public String savePost(
            @ModelAttribute PostEdit post,
            @RequestParam("image") MultipartFile image
    ) {
        Long savedId = postService.save(post, image);
        return "redirect:/posts/" + savedId;
    }

    @PostMapping("/{postId}/edit")
    public String editPost(
            @ModelAttribute PostEdit post,
            @RequestParam("image") MultipartFile image,
            @PathVariable long postId
    ) {
        post.setId(postId);
        Long savedId = postService.update(post, image);
        RedirectView redirect = new RedirectView("/posts");
        redirect.addStaticAttribute("id", savedId);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/like")
    public String like(
            @RequestParam boolean like,
            @PathVariable long postId
    ) {
        postService.like(postId, like);
        return "redirect:/posts/" + postId;
    }

}
