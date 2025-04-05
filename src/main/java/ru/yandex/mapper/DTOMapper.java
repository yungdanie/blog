package ru.yandex.mapper;

public interface DTOMapper<F, T> {
    T map(F from);
}
