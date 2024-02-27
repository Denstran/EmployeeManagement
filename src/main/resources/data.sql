INSERT INTO company_branch (
    company_branch_building_number,
    company_branch_budget,
    company_branch_city,
    company_branch_country,
    company_branch_phone_number,
    company_branch_street,
    company_branch_zip_code
) VALUES
      (1, '100000 RUB', 'City1', 'Country1', '123-456-7890', 'Street1', '12345'),
      (2, '150000 RUB', 'City2', 'Country2', '234-567-8901', 'Street2', '23456'),
      (3, '120000 RUB', 'City3', 'Country3', '345-678-9012', 'Street3', '34567'),
      (4, '80000 RUB', 'City4', 'Country4', '456-789-0123', 'Street4', '45678'),
      (5, '90000 RUB', 'City5', 'Country5', '567-890-1234', 'Street5', '56789'),
      (6, '110000 RUB', 'City6', 'Country6', '678-901-2345', 'Street6', '67890'),
      (7, '95000 RUB', 'City7', 'Country7', '789-012-3456', 'Street7', '78901'),
      (8, '130000 RUB', 'City8', 'Country8', '890-123-4567', 'Street8', '89012'),
      (9, '70000 RUB', 'City9', 'Country9', '901-234-5678', 'Street9', '90123'),
      (10, '80000 RUB', 'City10', 'Country10', '012-345-6789', 'Street10', '01234');

INSERT INTO department (department_name) VALUES
                                             ('Marketing'),
                                             ('Finance'),
                                             ('Human Resources'),
                                             ('Research and Development'),
                                             ('Sales');

INSERT INTO employee_statuses (employee_status) VALUES ('WORKING'), ('VACATION'), ('HEALING'), ('FIRED');

-- Marketing
INSERT INTO position (dep_id, position_name) VALUES
                                                 (1, 'Marketing Manager'),
                                                 (1, 'Marketing Coordinator'),
                                                 (1, 'Social Media Specialist'),
                                                 (1, 'Brand Manager'),
                                                 (1, 'Market Research Analyst');

-- Finance
INSERT INTO position (dep_id, position_name) VALUES
                                                 (2, 'Financial Analyst'),
                                                 (2, 'Accountant'),
                                                 (2, 'Finance Manager'),
                                                 (2, 'Auditor'),
                                                 (2, 'Tax Specialist');

-- Human Resources
INSERT INTO position (dep_id, position_name) VALUES
                                                 (3, 'HR Manager'),
                                                 (3, 'Recruiter'),
                                                 (3, 'Training Specialist'),
                                                 (3, 'Compensation Analyst'),
                                                 (3, 'Employee Relations Specialist');

-- Research and Development
INSERT INTO position (dep_id, position_name) VALUES
                                                 (4, 'Research Scientist'),
                                                 (4, 'Software Engineer'),
                                                 (4, 'Product Manager'),
                                                 (4, 'Quality Assurance Analyst'),
                                                 (4, 'Data Analyst');

-- Sales
INSERT INTO position (dep_id, position_name) VALUES
                                                 (5, 'Sales Manager'),
                                                 (5, 'Account Executive'),
                                                 (5, 'Sales Representative'),
                                                 (5, 'Business Development Manager'),
                                                 (5, 'Customer Success Manager');

INSERT INTO department_info(company_branch_id, department_id, department_budget) VALUES
                                                                                     (1, 1, '10000 RUB'),
                                                                                     (1, 2, '10000 RUB'),
                                                                                     (1, 3, '10000 RUB'),
                                                                                     (1, 4, '10000 RUB'),
                                                                                     (1, 5, '10000 RUB');

INSERT INTO employee (employment_date, home_building_number, company_branch_id, employee_status_id, position_id, salary, email, first_name, home_city, home_country, home_street, home_zip_code, last_name, middle_name, phone_number)
VALUES
    ('2023-05-15', 123, 1, 1, 1, '50000 RUB', 'john.doe@examle.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4567'),
    ('2023-05-15', 123, 1, 1, 2, '50000 RUB', 'john.doe@exmple.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4561'),
    ('2023-05-15', 123, 1, 1, 3, '50000 RUB', 'john.doe@exampe.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4562'),
    ('2023-05-15', 123, 1, 1, 4, '50000 RUB', 'john.doe@xample.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4563'),
    ('2023-05-15', 123, 1, 1, 5, '50000 RUB', 'john.doe@ample.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4564'),
    ('2023-05-15', 123, 1, 1, 6, '50000 RUB', 'john.doe@exale.com', 'John', 'New York', 'USA', '123 Main St', '10001', 'Doe', 'Robert', '555-123-4565');

INSERT INTO PAYMENT_TYPE (PAYMENT_TYPE) VALUES
                                            ('SALARY'),
                                            ('BONUS'),
                                            ('BUDGET_CHANGES'),
                                            ('OTHERS')


