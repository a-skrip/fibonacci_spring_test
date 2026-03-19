CREATE TABLE user_achievements
(
    id                   SERIAL primary key,
    chat_id              bigint NOT NULL,
    is_startupper        boolean default false,
    date_startupper      timestamp,
    is_worker_night      boolean default false,
    date_worker_night    timestamp,
    is_marathon_runner   boolean default false,
    date_marathon_runner timestamp
)