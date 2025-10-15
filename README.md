# User Segment Service

## Описание проекта

Этот проект представляет собой REST API сервис для управления пользователями и их сегментами. 
Сервис позволяет создавать, изменять и удалять сегменты, а также добавлять пользователей в 
сегменты или удалять их из сегментов.

Основная фишка сервиса - возможность случайным образом распределить сегмент на определенный 
процент пользователей. Например, можно добавить сегмент "MAIL_GPT" для 30% всех пользователей в системе.

## Технологии

В проекте использованы следующие технологии:
- Spring Boot
- Spring Web (для создания REST API)
- Spring Data JPA (для работы с базой данных)
- PostgreSQL (в качестве базы данных)
- Lombok (для уменьшения шаблонного кода)
- Swagger/OpenAPI (для документации API)

## Запуск проекта

1. Клонировать репозиторий

   ```bash
   git clone https://github.com/ZEBLIVER/user-segment-service.git

2. Настроить подключение к базе данных в `application.properties`
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/user_segment_db 
   spring.datasource.username=postgres 
   spring.datasource.password=password

3. Запустить приложение
   ```bash
   ./mvnw spring-boot:run

4. Открыть Swagger UI
   ```bash
   http://localhost:8090/docs

## API Endpoints

### Пользователи
- `POST /api/users` - Добавить нового пользователя
- `GET /api/users/all` - Получить список всех пользователей
- `GET /api/users/{userId}/segments` - Получить список всех сегментов пользователя по его ID

### Сегменты
- `POST /api/segments` - Добавить новый сегмент
- `DELETE /api/segments/{segmentName}` - Удалить сегмент
- `PUT /api/segments/rename/{segmentName}` - Переименовать сегмент
- `GET /api/segments/{id}` - Получить сегмент по ID

### Управление пользователями и сегментами
- `PUT /api/segments/{segmentName}/user/{userId}` - Добавить пользователя в сегмент
- `DELETE /api/segments/{segmentName}/user/{userId}` - Удалить пользователя из сегмента
- `POST /api/segments/distribute/{segmentName}` - Распределить сегмент на определенный процент пользователей (с телом запроса: Double percent)

## Структура проекта

- `controller` - REST контроллеры
- `model` - Модели данных (User, Segment)
- `repository` - Репозитории для работы с базой данных
- `service` - Сервисы с бизнес-логикой

