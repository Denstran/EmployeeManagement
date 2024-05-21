INSERT INTO company_branch (
    company_branch_building_number,
    company_branch_budget,
    company_branch_city,
    company_branch_country,
    company_branch_phone_number,
    company_branch_street,
    company_branch_zip_code, deleted
) VALUES
      (1, 100000.0, 'City1', 'Country1', '123-456-7890', 'Street1', '12345', false),
      (2, 150000.0, 'City2', 'Country2', '234-567-8901', 'Street2', '23456', false),
      (3, 120000.0, 'City3', 'Country3', '345-678-9012', 'Street3', '34567', false),
      (4, 80000.0, 'City4', 'Country4', '456-789-0123', 'Street4', '45678', false),
      (5, 90000.0, 'City5', 'Country5', '567-890-1234', 'Street5', '56789', false),
      (6, 110000.0, 'City6', 'Country6', '678-901-2345', 'Street6', '67890', false),
      (7, 95000.0, 'City7', 'Country7', '789-012-3456', 'Street7', '78901', false),
      (8, 130000.0, 'City8', 'Country8', '890-123-4567', 'Street8', '89012', true),
      (9, 70000.0, 'City9', 'Country9', '901-234-5678', 'Street9', '90123', true),
      (10, 80000.0, 'City10', 'Country10', '012-345-6789', 'Street10', '01234', true);

INSERT INTO department (department_name, department_type) VALUES
                                                              ('Marketing', 'OTHER'),
                                                              ('Finance', 'OTHER'),
                                                              ('Human Resources', 'HR'),
                                                              ('Research and Development', 'OTHER'),
                                                              ('Sales', 'OTHER');


-- Marketing
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (1, 'Marketing Manager', true),
                                                             (1, 'Marketing Coordinator', false),
                                                             (1, 'Social Media Specialist', false),
                                                             (1, 'Brand Manager', false),
                                                             (1, 'Market Research Analyst', false);

-- Finance
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (2, 'Financial Analyst', false),
                                                             (2, 'Accountant', false),
                                                             (2, 'Finance Manager', true),
                                                             (2, 'Auditor', false),
                                                             (2, 'Tax Specialist', false);

-- Human Resources
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (3, 'HR Manager', true),
                                                             (3, 'Recruiter', false),
                                                             (3, 'Training Specialist', false),
                                                             (3, 'Compensation Analyst', false),
                                                             (3, 'Employee Relations Specialist', false);

-- Research and Development
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (4, 'Research Scientist', false),
                                                             (4, 'Software Engineer', false),
                                                             (4, 'Product Manager', true),
                                                             (4, 'Quality Assurance Analyst', false),
                                                             (4, 'Data Analyst', false);

-- Sales
INSERT INTO position (dep_id, position_name, is_leading) VALUES
                                                             (5, 'Sales Manager', true),
                                                             (5, 'Account Executive', false),
                                                             (5, 'Sales Representative', false),
                                                             (5, 'Business Development Manager', false),
                                                             (5, 'Customer Success Manager', false);

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
                                               (3, 8);

INSERT INTO employee (employment_date, home_building_number, company_branch_id, employee_status, position_id, salary, email, first_name, home_city, home_country, home_street, home_zip_code, last_name, middle_name, phone_number, vacation_days, user_id)
VALUES
    ('2021-05-15', 123, 1, 'WORKING', 1, 50000.0, 'john.doe@examle.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4567', 100, 2),
    ('2020-05-15', 123, 1, 'FIRED', 2, 25000.0, 'john.doe@exmple.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4561', 28, 3),
    ('2024-05-15', 123, 1, 'WORKING', 3, 150000.0, 'john.doe@exampe.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4562', 28, 4),
    ('2022-05-15', 123, 1, 'VACATION', 4, 10000.0, 'john.doe@xample.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4563', 28, 5),

    ('2021-05-15', 123, 1, 'WORKING', 5, 50000.0, 'john.doe@sail.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-133-4564', 28, 6),
    ('2020-05-15', 123, 1, 'WORKING', 11, 50000.0, 'john.doe@gail.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-143-4564', 28, 7),
    ('2023-05-15', 123, 1, 'WORKING', 12, 50000.0, 'john.doe@wail.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-153-4564', 28, 8),
    ('2022-05-15', 123, 1, 'WORKING', 13, 50000.0, 'john.doe@lain.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-163-4564', 28, 9),
    ('2024-05-04', 123, 1, 'WORKING', 14, 50000.0, 'john.doe@kain.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-173-4564', 28, 10),
    ('2019-05-15', 123, 1, 'WORKING', 4, 50000.0, 'john.doe@fain.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-183-4564', 28, 11),
    ('2022-05-15', 123, 1, 'WORKING', 5, 50000.0, 'john.doe@oali.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-193-4564', 28, 12),
    ('2023-05-15', 123, 1, 'WORKING', 2, 50000.0, 'john.doe@rail.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-223-4564', 28, 13),
    ('2022-05-15', 123, 1, 'WORKING', 3, 50000.0, 'john.doe@pail.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-323-4564', 28, 14),
    ('2021-05-15', 123, 1, 'WORKING', 4, 50000.0, 'john.doe@amle.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-423-4564', 28, 15),
    ('2023-05-15', 123, 1, 'WORKING', 5, 50000.0, 'john.doe@mail.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-523-4564', 28, 16),
    ('2023-05-15', 123, 1, 'WORKING', 6, 50000.0, 'john.doe@exale.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-623-4565', 28, 17),
    ('2021-05-15', 123, 2, 'WORKING', 1, 50000.0, 'john.doeAnotherBranch@examle.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-723-4567', 100, 18),
    ('2021-05-15', 123, 1, 'WORKING', 8, 50000.0, 'john.accounter@examle.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-823-4567', 100, 19);

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