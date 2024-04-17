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

INSERT INTO employee (employment_date, home_building_number, company_branch_id, employee_status, position_id, salary, email, first_name, home_city, home_country, home_street, home_zip_code, last_name, middle_name, phone_number)
VALUES
    ('2023-05-15', 123, 1, 'WORKING', 1, 50000.0, 'john.doe@examle.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4567'),
    ('2020-05-15', 123, 1, 'FIRED', 2, 25000.0, 'john.doe@exmple.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4561'),
    ('2024-05-15', 123, 1, 'VACATION', 3, 150000.0, 'john.doe@exampe.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4562'),
    ('2022-05-15', 123, 1, 'VACATION', 4, 10000.0, 'john.doe@xample.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4563'),
    ('2023-05-15', 123, 1, 'WORKING', 5, 50000.0, 'john.doe@ample.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4564'),
    ('2023-05-15', 123, 1, 'WORKING', 6, 50000.0, 'john.doe@exale.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4565');

INSERT INTO UZER (uzer_name, password, IS_ENABLED) VALUES ( 'admin', '$2a$12$kprqzMBw3sRsWU4s1Svlwuqz/b29gQ/q1h2f7H6k./xAuFqwR8PJu', true)


