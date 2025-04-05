package ru.yandex.util;

public record Pageable(long pageNumber, long pageSize, boolean hasNext) { }
