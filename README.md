# codemark-task
## Создание базы данных
1. Создать базу данных с названием "codemark" или изменить название БД в application.properties
2. Запустить скрипт codemark_database.sql
## Нюансы реализации
1. Сервисы /get, /delete, /edit работает по логину.
2. В случае, если в JSON /edit не будет отправлен какой-либо атрибут пользователя или массив ролей, он не будет изменяться. В случае, если один из отправляемых атрибутов не соответствует какому-либо критерию, редактирование совершено не будет, в ответе будут отправлены сообщения об ошибках. 
3. В случае, если в БД не существует сущность роли с ID, которая указана в JSON запросе, тогда эти сущности будут фильтроваться и не учитываться.
4. В методах /delete и /edit, в случае если пользователь с отправленным логином не существует, удаление/изменение не произойдет, в ответе будет указана ошибка. В методе /add, в случае, если пользователь с отправленным логином существует, добавление в БД не произойдет, в ответе также будет указана ошибка
