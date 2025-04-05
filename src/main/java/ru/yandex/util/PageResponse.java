package ru.yandex.util;

import java.util.List;

public record PageResponse(List<Long> ids, boolean hasNext) {}
