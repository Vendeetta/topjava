DELETE
FROM user_role;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2023-04-12 10:00:00', 'Завтрак', 500),
       (100000, '2023-04-16 11:00:00', 'Обед', 500),
       (100000, '2023-04-12 13:00:00', 'Ужин', 999),
       (100001, '2023-04-12 10:00:00', 'ЗавтраАдмин', 750),
       (100001, '2023-04-12 11:00:00', 'ОбедАдмин', 250),
       (100001, '2023-04-12 12:00:00', 'УжинАдмин', 300);