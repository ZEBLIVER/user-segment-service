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
   http://localhost:8090/swagger-ui.html

## API Endpoints

### Пользователи
- `POST /addUser` - Добавить нового пользователя
- `DELETE /deleteUser/{id}` - Удалить пользователя
- `GET /findAllUsers` - Получить список всех пользователей

### Сегменты
- `POST /addSegment` - Добавить новый сегмент
- `DELETE /delSegment/{id}` - Удалить сегмент
- `PUT /renameSegment/{id}` - Переименовать сегмент
- `GET /findAllSegments` - Получить список всех сегментов

### Управление пользователями и сегментами
- `POST /addUserToSegment/{userId}` - Добавить пользователя в сегмент
- `DELETE /removeUserFromSegment/{userId}` - Удалить пользователя из сегмента
- `POST /distributeSegment` - Распределить сегмент на определенный процент пользователей
- `GET /userSegments/{userId}` - Получить список сегментов пользователя

## Структура проекта

- `controller` - REST контроллеры
- `model` - Модели данных (User, Segment)
- `repository` - Репозитории для работы с базой данных
- `service` - Сервисы с бизнес-логикой

