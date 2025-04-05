package ru.yandex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.service.CommentService;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/edit/{commentId}")
    public String editComment(
            @RequestParam("text") String commentText,
            @PathVariable("postId") long postId,
            @PathVariable long commentId
    ) {
        commentService.update(commentId, commentText);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/add")
    public String addComment(
            @RequestParam("text") String commentText,
            @PathVariable("postId") long postId
    ) {
        commentService.save(postId, commentText);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/delete/{commentId}")
    public String deleteComment(
            @PathVariable long postId,
            @PathVariable long commentId
    ) {
        commentService.delete(commentId);
        return "redirect:/posts/" + postId;
    }
}
