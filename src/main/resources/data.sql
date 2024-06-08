INSERT INTO company_branch (
    company_branch_building_number,
    company_branch_budget,
    company_branch_city,
    company_branch_country,
    company_branch_phone_number,
    company_branch_street,
    company_branch_zip_code,
    deleted
) VALUES
      (1, 100000.0, 'Москва', 'Россия', '123-456-7890', 'Ленинградский проспект', '125284', false),
      (2, 150000.0, 'Санкт-Петербург', 'Россия', '234-567-8901', 'Невский проспект', '191025', false),
      (3, 120000.0, 'Новосибирск', 'Россия', '345-678-9012', 'Красный проспект', '630099', false),
      (4, 80000.0, 'Екатеринбург', 'Россия', '456-789-0123', 'Проспект Ленина', '620014', false),
      (5, 90000.0, 'Казань', 'Россия', '567-890-1234', 'Проспект Победы', '420100', false),
      (6, 110000.0, 'Нижний Новгород', 'Россия', '678-901-2345', 'Улица Белинского', '603024', false),
      (7, 95000.0, 'Челябинск', 'Россия', '789-012-3456', 'Проспект Ленина', '454091', false),
      (8, 130000.0, 'Самара', 'Россия', '890-123-4567', 'Московское шоссе', '443028', true),
      (9, 70000.0, 'Ростов-на-Дону', 'Россия', '901-234-5678', 'Проспект Будённовский', '344002', true),
      (10, 80000.0, 'Уфа', 'Россия', '012-345-6789', 'Проспект Октября', '450077', true);


INSERT INTO department (department_name, department_type) VALUES
                                                              ('Маркетинг', 'OTHER'),
                                                              ('Финансы', 'ACCOUNTING'),
                                                              ('Человеческие ресурсы', 'HR'),
                                                              ('Исследования и разработки', 'OTHER'),
                                                              ('Продажи', 'OTHER');

-- Маркетинг
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (1, 'Менеджер по маркетингу', true),
                                                             (1, 'Координатор по маркетингу', false),
                                                             (1, 'Специалист по социальным медиа', false),
                                                             (1, 'Бренд-менеджер', false),
                                                             (1, 'Аналитик по исследованию рынка', false);

-- Финансы
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (2, 'Финансовый аналитик', false),
                                                             (2, 'Бухгалтер', false),
                                                             (2, 'Финансовый менеджер', true),
                                                             (2, 'Аудитор', false),
                                                             (2, 'Налоговый специалист', false);

-- Человеческие ресурсы
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (3, 'Менеджер по персоналу', true),
                                                             (3, 'Рекрутер', false),
                                                             (3, 'Специалист по обучению', false),
                                                             (3, 'Аналитик по компенсациям', false),
                                                             (3, 'Специалист по трудовым отношениям', false);

-- Исследования и разработки
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (4, 'Научный сотрудник', false),
                                                             (4, 'Инженер-программист', false),
                                                             (4, 'Менеджер продукта', true),
                                                             (4, 'Аналитик по обеспечению качества', false),
                                                             (4, 'Аналитик данных', false);

-- Продажи
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (5, 'Менеджер по продажам', true),
                                                             (5, 'Аккаунт-менеджер', false),
                                                             (5, 'Специалист по продажам', false),
                                                             (5, 'Специалист по развитию бизнеса', false),
                                                             (5, 'Специалист по успеху клиентов', false);


INSERT INTO department_info(company_branch_id, department_id, department_budget) VALUES
                                                                                     (1, 1, 10000.0),
                                                                                     (1, 2, 10000.0),
                                                                                     (1, 3, 10000.0),
                                                                                     (1, 4, 10000.0),
                                                                                     (1, 5, 10000.0);

INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'admin', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);

INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@examle.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@exmple.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@exampe.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@xample.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@sail.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@gail.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@wail.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@lain.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@kain.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@fain.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@oali.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@rail.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@pail.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@amle.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@mail.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doe@exale.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.doeAnotherBranch@examle.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);
INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'john.accounter@examle.com', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true);

INSERT INTO user_roles (roles, user_id) values (0, 1),
                                               (1, 18),
                                               (4, 3),
                                               (4, 4),
                                               (4, 5),
                                               (1, 6),
                                               (1, 2),
                                               (2, 2),
                                               (3, 2),
                                               (4, 2),
                                               (2, 2),
                                               (2, 19),
                                               (2, 19),
                                               (3, 8);

INSERT INTO employee (employment_date, home_building_number, company_branch_id, employee_status, position_id, salary, email, first_name, home_city, home_country, home_street, home_zip_code, last_name, middle_name, phone_number, vacation_days, user_id)
VALUES
    ('2021-05-15', 123, 1, 'WORKING', 1, 50000.0, 'john.doe@examle.com', 'Иван', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Иванов', 'Алексеевич', '555-123-4567', 100, 2),
    ('2020-05-15', 123, 1, 'WORKING', 2, 25000.0, 'john.doe@exmple.com', 'Петр', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Петров', 'Иванович', '555-123-4561', 28, 3),
    ('2024-05-15', 123, 1, 'WORKING', 3, 150000.0, 'john.doe@exampe.com', 'Алексей', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Сидоров', 'Петрович', '555-123-4562', 28, 4),
    ('2022-05-15', 123, 1, 'VACATION', 4, 10000.0, 'john.doe@xample.com', 'Дмитрий', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Федоров', 'Алексеевич', '555-123-4563', 28, 5),

    ('2021-05-15', 123, 1, 'WORKING', 5, 50000.0, 'john.doe@sail.com', 'Сергей', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Егоров', 'Дмитриевич', '555-133-4564', 28, 6),
    ('2020-05-15', 123, 1, 'WORKING', 11, 50000.0, 'john.doe@gail.com', 'Николай', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Захаров', 'Сергеевич', '555-143-4564', 28, 7),
    ('2023-05-15', 123, 1, 'WORKING', 12, 50000.0, 'john.doe@wail.com', 'Владимир', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Кузнецов', 'Николаевич', '555-153-4564', 28, 8),
    ('2022-05-15', 123, 1, 'WORKING', 13, 50000.0, 'john.doe@lain.com', 'Виктор', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Попов', 'Владимирович', '555-163-4564', 28, 9),
    ('2024-05-04', 123, 1, 'WORKING', 14, 50000.0, 'john.doe@kain.com', 'Роман', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Смирнов', 'Викторович', '555-173-4564', 28, 10),
    ('2019-05-15', 123, 1, 'WORKING', 4, 50000.0, 'john.doe@fain.com', 'Евгений', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Михайлов', 'Романович', '555-183-4564', 28, 11),
    ('2022-05-15', 123, 1, 'WORKING', 5, 50000.0, 'john.doe@oali.com', 'Михаил', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Фролов', 'Евгеньевич', '555-193-4564', 28, 12),
    ('2023-05-15', 123, 1, 'WORKING', 2, 50000.0, 'john.doe@rail.com', 'Андрей', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Воробьев', 'Михайлович', '555-223-4564', 28, 13),
    ('2022-05-15', 123, 1, 'WORKING', 3, 50000.0, 'john.doe@pail.com', 'Василий', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Лебедев', 'Андреевич', '555-323-4564', 28, 14),
    ('2021-05-15', 123, 1, 'WORKING', 4, 50000.0, 'john.doe@amle.com', 'Константин', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Козлов', 'Васильевич', '555-423-4564', 28, 15),
    ('2023-05-15', 123, 1, 'WORKING', 5, 50000.0, 'john.doe@mail.com', 'Анатолий', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Новиков', 'Константинович', '555-523-4564', 28, 16),
    ('2023-05-15', 123, 1, 'WORKING', 6, 50000.0, 'john.doe@exale.com', 'Юрий', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Морозов', 'Анатольевич', '555-623-4565', 28, 17),
    ('2021-05-15', 123, 2, 'WORKING', 1, 50000.0, 'john.doeAnotherBranch@examle.com', 'Игорь', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Савельев', 'Юрьевич', '555-723-4567', 100, 18),
    ('2021-05-15', 123, 1, 'WORKING', 8, 50000.0, 'john.accounter@examle.com', 'Геннадий', 'Москва', 'Россия', 'Ленина ул.', '101000', 'Чернов', 'Игоревич', '555-823-4567', 100, 19);

-- Employee Payment Log
INSERT INTO employee_payment_log (date_of_payment, payment_amount, employee_id, payment_type, transfer_action)
VALUES
    ('2024-03-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-03-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-03-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-02-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-01-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-05-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-05-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-05-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-05-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-05-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-05-01', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2023-05-05', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2023-05-05', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2023-05-05', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2023-05-05', 5000.00, 1, 'SALARY', 'INCREASE'),
    ('2024-05-01', 6000.00, 2, 'SALARY', 'INCREASE'),
    ('2024-05-01', 7000.00, 3, 'SALARY', 'INCREASE'),
    ('2024-05-01', 8000.00, 4, 'SALARY', 'INCREASE'),
    ('2024-05-01', 9000.00, 5, 'SALARY', 'INCREASE'),
    ('2024-05-01', 10000.00, 6, 'SALARY', 'INCREASE'),
    ('2024-05-01', 11000.00, 7, 'SALARY', 'INCREASE'),
    ('2024-05-01', 12000.00, 8, 'SALARY', 'INCREASE'),
    ('2024-05-01', 13000.00, 9, 'SALARY', 'INCREASE'),
    ('2024-05-01', 14000.00, 10, 'SALARY', 'INCREASE');