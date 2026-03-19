CREATE TABLE user_sessions
(
    id        SERIAL primary key,
    chat_id   bigint        not null,
    type      varchar(10) not null,
    duration  int,
    start_at  timestamp   not null,
    stop_at   timestamp,
    completed boolean     not null
);
