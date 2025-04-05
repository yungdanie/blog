# Blog App

Простое веб-приложение для ведения блога, разработанное в рамках проектной работы [Яндекс Практикум](https://practicum.yandex.ru/).

## 🌟 Основные возможности

- ✍️ Создание и удаление постов
- 🔍 Просмотр ленты с пагинацией
- 💬 Написание комментариев
- ❤️ Система лайков/дизлайков
- 🏷️ Фильтрация по тегам

## 🛠 Технологический стек

**Backend:**
- Spring Data JDBC
- Spring MVC
- Hibernate
- Junit5

**Frontend:**
- Thymeleaf
- JavaScript

**Database**
- H2 (тесты)
- PostgreSQL

**Инструменты:**
- Maven
- Git

## 🚀 Запуск проекта

### Требования
- Java 21
- PostgreSQL 14+
- Maven 3.8+

### Установка
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/yungdanie/blog.git
2. Установите конфигурацию application.properties:
   - spring.datasource.url
   - spring.datasource.username
   - spring.datasource.password
3. Сборка
   ```
   ./mvnw clean package
   ```
   Готовый архив лежит в ./target/
4. Развертывание в контейнер сервлетов (например Tomcat)