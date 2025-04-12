## Build Instructions

1.  Клонируйте репозиторий: `git clone https://github.com/Badlose/Starbank`
2.  Перейдите в директорию проекта: `cd your-project`
3.  Соберите JAR-архив: `mvn clean install`

JAR-архив будет создан в директории `target`.

## Run Instructions

1.  Установите необходимые переменные среды (см. раздел "Configuration").
2.  Запустите приложение: `java -jar target/your-project.jar`


## Configuration

Необходимые переменные среды Spring Boot:

*   `SPRING_DATASOURCE_URL`: URL базы данных PostgreSQL (например, `jdbc:postgresql://localhost:5432/your_database`)
*   `SPRING_DATASOURCE_USERNAME`: Имя пользователя базы данных
*   `SPRING_DATASOURCE_PASSWORD`: Пароль пользователя базы данных
*   `TELEGRAM_BOT_TOKEN`: Токен вашего Telegram-бота (если используется)
