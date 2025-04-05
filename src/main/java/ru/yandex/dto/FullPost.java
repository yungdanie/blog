package ru.yandex.dto;

import java.util.List;
import java.util.StringJoiner;

public record FullPost(
        long id,
        String title,
        String text,
        List<String> tags,
        List<CommentDTO> comments,
        long likesCount
) {

    public String[] getTextParts() {
        return text.split(System.lineSeparator());
    }

    public String getTagsAsText() {
        StringJoiner joiner = new StringJoiner(" ");
        tags.forEach(joiner::add);
        return joiner.toString();
    }
}
