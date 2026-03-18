CREATE TABLE user_achievements
(
    id          SERIAL primary key,
    achievement varchar(40),
    date        timestamp
)