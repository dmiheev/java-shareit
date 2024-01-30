DELETE FROM comments CASCADE;
DELETE FROM bookings CASCADE;
DELETE FROM items CASCADE;
DELETE FROM requests CASCADE;
DELETE FROM users CASCADE;

INSERT INTO users (id, name, email)
VALUES (1, 'Name 1', 'name1@mail.ru'),
       (2, 'Name 2', 'name2@mail.com'),
       (3, 'Name 3', 'name3@mail.com');

INSERT INTO requests(id, description, requester_id, creation_date)
VALUES (1, 'Отвертка', 2, '2023-07-10 20:20:20'),
       (2, 'Пила', 2, '2023-07-11 10:20:20'),
       (3, 'Стремянка', 2, '2023-07-12 10:20:20');

INSERT INTO items (id, name, owner_id, description, is_available, request_id)
VALUES (1, 'Отвертка', 1, 'Простая отвертка', 'true', 1),
       (2, 'Топор', 1, 'Простой топор', 'true', 1),
       (3, 'Пила', 1, 'Бензопила', 'false', 2);

INSERT INTO bookings (id, booker_id, item_id, start_date, end_date, status)
VALUES (1, 2, 1, '2023-07-08 20:20:20', '2023-07-08 21:20:20', 'APPROVED'),
       (2, 2, 1, '2030-07-13 20:20:20', '2030-07-13 21:20:20', 'APPROVED'),
       (3, 2, 1, '2023-07-11 20:20:20', '2029-07-14 21:20:20', 'APPROVED'),
       (4, 2, 1, '2030-07-14 22:20:20', '2030-07-15 21:20:20', 'CANCELED');

INSERT INTO comments (id, author_id, item_id, text, created)
VALUES (2, 2, 1, 'comment text', '2023-07-11 12:20:20');

