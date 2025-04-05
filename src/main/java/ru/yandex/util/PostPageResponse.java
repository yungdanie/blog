package ru.yandex.util;

import ru.yandex.dto.PostPreview;

import java.util.List;

public record PostPageResponse(List<PostPreview>postPreviews, Pageable pageable) {}
