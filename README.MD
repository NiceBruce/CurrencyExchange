# Currency Exchange Project 💱

## Описание

REST API для управления валютами и обменными курсами. Поддержка просмотра, добавления, редактирования, а также конвертации валют.

## Технологии

* Java 11
* Tomcat 10
* SQLite
* Maven

## Архитектура

Приложение разделено на слои:

* **DAO (Data Access Object)** - отвечает за доступ к данным в базе данных.
* **Service** - содержит бизнес-логику приложения, включая преобразование сущностей (entity) в DTO (Data Transfer Object) и обратно.
* **Controller** - обрабатывает запросы от клиента и возвращает ответы.

## Ссылки

* [Развернутый проект Currency Exchange](http://rtfmyoumust.ru/currency_exchange)
* [REST API документация](http://rtfmyoumust.ru/currency_exchange/rest_api_documentation.html)

## Дополнительно
* В проекте присутствует postman-коллекция для теста API