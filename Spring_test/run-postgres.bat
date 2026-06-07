@echo off
REM Запускаем контейнер PostgreSQL в фоновом режиме на Windows

docker run --detach ^
  --name fibonacci-postgres ^
  --env POSTGRES_DB=fibonacci_db ^
  --env POSTGRES_USER=postgres ^
  --env POSTGRES_PASSWORD=postgres ^
  --publish 5435:5432 ^
  postgres:17.5
