create table if not exists post
(
    id          bigserial primary key,
    title       varchar(256) not null,
    text        text         not null,
    likes_count bigint       not null
);

create table if not exists tag
(
    id      bigserial primary key,
    name    varchar(256)                not null,
    post_id bigint references post (id) on delete cascade not null
);

create table if not exists comment
(
    id      bigserial primary key,
    text    text                        not null,
    post_id bigint references post (id) on delete cascade not null
);

create table if not exists image
(
    id      bigserial primary key,
    data    bytea,
    post_id bigint references post (id) on delete cascade not null
);