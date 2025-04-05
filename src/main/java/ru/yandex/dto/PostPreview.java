package ru.yandex.dto;

import java.util.List;

public record PostPreview(
        Long id,
        String title,
        String textPreview,
        long likesCount,
        long commentsCount,
        List<String> tags
) {}
