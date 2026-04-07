# Платформа отзывов о фильмах

Стартовый REST API для платформы отзывов о фильмах, включает PostgreSQL с миграциям
Flyway и документацией OpenAPI. Пока без Spring Security — следующая задача по проекту это
добавить авторизацию и аутентификацию. Сейчас используется временный заголовок X-User-Id и проверяется при обновлении/удалении записей.


## Стек

- Java 17
- Spring Boot 3.x
- Gradle Kotlin DSL
- Зависимости: Web, Data JPA, драйвер PostgreSQL, Flyway, springdoc-openapi

## Быстрый старт

1. **Запустите PostgreSQL**

```bash
docker compose up -d
```

Это поднимет Postgres на `localhost:5432` с БД `moviereviews`
и пользователем/паролем `moviereviews`.


2. **Запустите приложение**

```bash
# с локальным Gradle (рекомендуется 8.6+)
./gradlew bootRun
# или соберите jar
./gradlew build
java -jar build/libs/moviereviews-0.0.1-SNAPSHOT.jar
```

Для Windows используйте `gradlew.bat` вместо `./gradlew`.

3. **API документация (Swagger UI)**

Откройте http://localhost:8080/swagger-ui.html

## Миграции и начальные данные

- `V1__init.sql` — создает таблицы `users`, `movies`, `reviews` + индексы.
- `V2__seed.sql` — начальные данные:
  - users: alice (`11111111-1111-1111-1111-111111111111`), bob (`22222222-2222-2222-2222-222222222222`)
  - несколько фильмов и отзывов

## Доступ к ресурсам

Пока Security не добавлена, API берёт текущего пользователя из
заголовка `X-User-Id`. Если заголовок опущен, по умолчанию
используется UUID Алисы из начальных данных.

- Создание отзыва использует текущего пользователя (из заголовка или Алису).
- Обновление/удаление отзыва требуют владения; иначе вернётся 403.


## Ключевые URL запросов

### Фильмы
- `GET /api/movies` — список (с пагинацией; поддерживает page, size, sort)
- `GET /api/movies/{id}` — получить по id
- `POST /api/movies` — создать
- `PUT /api/movies/{id}` — обновить
- `DELETE /api/movies/{id}` — удалить

### Reviews
- `GET /api/movies/{movieId}/reviews` — список отзывов к фильму (пагинация)
- `GET /api/reviews/{id}` — получить отзыв
- `POST /api/reviews` — создать отзыв (использует текущего пользователя)
- `PUT /api/reviews/{id}` — обновить отзыв (только владелец)
- `DELETE /api/reviews/{id}` — удалить отзыв (только владелец)

### Users
- `POST /api/users/register` — регистрация пользователя (пока без аутентификации)

## Примечания по добавлению Security

- Замените `CurrentUserProvider` на principal из Spring Security `Authentication`.
- Удалите параметр `X-User-Id` из контроллеров и используйте user id из principal.
- Хэшируйте пароли при регистрации и обеспечьте уникальность имён пользователей (уже предусмотрено).
