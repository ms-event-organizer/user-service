# Микросервис user-service
Микросервис для работы с пользователями.

Модель User включает следующие поля: name, email, password, aboutMe.

Данный микросервис позволяет создавать пользователей, обновлять информацию о них (кроме email), получить пользователя по id (если пользователь запрашивает сам себя, он также сможет получить свой пароль), получить список всех пользователей (без паролей), а также удалить пользователя по id.

Хэширование пароля реализовано с использованием схемы OpenBSD bcrypt.

Микросервис написан на Java 21 и Spring Boot 3.

## Микросервис разрабатывали:
• [Виктор Вагнер] (https://github.com/Vagner-Viktor);

• [Галина Лобачёва] (https://github.com/KoshanSky1).

## Технологический стек:



## Эндпоинты:
• POST /users - создание пользователя;

• PATCH /users - обновление пользователя;

• GET /users/{id} - получение пользователя по id;

• GET /users?page={page}&size={size} - получение списка всех пользователей с пагинацией;

• DELETE /users - удаление пользователя по id.

## Запустить микросервис можно двумя способами.
### Первый способ:
•	Предварительно создать БД POSTGRES не ниже версии 15 с именем "mus".

•	Установить и запустить Docker. Необходимо для выполнения тестов с использованием test-containers.

•	Собрать jar файл при помощи maven командой mvn clean install.

•	Запустить микросервис командой java -jar user-service.jar.

•	Микросервис будет доступен по адресу https:/localhost.

### Второй способ:
•	Установить и запустить Docker.

•	Запустить контейнер командой docker compose up.

•	Микросервис будет доступен по адресу https:/localhost.
