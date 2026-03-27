CREATE TABLE IF NOT EXISTS news (
    id BIGSERIAL PRIMARY KEY ,
    category_id BIGINT ,
    title VARCHAR(100),
    text_news VARCHAR(255),
    creation_time TIMESTAMP,
    CONSTRAINT fk_category_id foreign key (category_id) references categories(id)
)
