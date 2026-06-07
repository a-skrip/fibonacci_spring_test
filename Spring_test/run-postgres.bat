@echo off
REM Запускаем контейнер PostgreSQL в фоновом режиме на Windows

docker run --detach ^
  --name fibonacci-postgres ^
  --env POSTGRES_DB=fibonacci_db ^
  --env POSTGRES_USER=user ^
  --env POSTGRES_PASSWORD=Ad0ZfRbh6UX9 ^
  --publish 5432:5432 ^
  postgres:17.5
