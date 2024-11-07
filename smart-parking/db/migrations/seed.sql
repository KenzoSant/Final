CREATE TYPE role_type AS ENUM ('CLIENT', 'ADMIN');
 
CREATE TABLE users(
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	role role_type NOT NULL DEFAULT 'CLIENT',
	enabled boolean NOT NULL DEFAULT TRUE
);
 
CREATE TABLE parkings(
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	enabled boolean NOT NULL DEFAULT TRUE
);
 
CREATE TABLE color(
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	enabled boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE make_car(
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	enabled BOOLEAN NOT NULL DEFAULT TRUE
);
 
CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    client_id INT NOT NULL,
    make_id INT NOT NULL,
    model VARCHAR(50) NOT NULL,
    plate VARCHAR(7) NOT NULL UNIQUE,
    year INT NOT NULL CHECK (YEAR > 1885),
    color_id INT NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (client_id) REFERENCES users(id),
    FOREIGN KEY (make_id) REFERENCES make_car(id),
    FOREIGN KEY (color_id) REFERENCES color(id)
);
 
CREATE TABLE parking_records(
	id SERIAL PRIMARY KEY,
	vehicles_id INT NOT NULL,
	parking_id INT NOT NULL,
	entry_time TIMESTAMP NOT NULL,
	exit_time TIMESTAMP,
	FOREIGN KEY (vehicles_id) REFERENCES vehicles(id),
    FOREIGN KEY (parking_id) REFERENCES parkings(id)
);
 
CREATE TABLE parking_prices (
    id SERIAL PRIMARY KEY,
    parking_id INT NOT NULL,
    fixed_rate DECIMAL(10, 2) NOT NULL,
    extra_hours_rate DECIMAL(10, 2) NOT NULL,
    start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP,
    FOREIGN KEY (parking_id) REFERENCES parkings(id)
);

CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    parking_records_id INT NOT NULL,
    parking_prices_id INT NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    pix_code TEXT NOT NULL,   
    url_qrcode  TEXT NOT NULL,
    CONSTRAINT fk_parking_records
        FOREIGN KEY (parking_records_id) REFERENCES parking_records (id),
    CONSTRAINT fk_parking_prices
        FOREIGN KEY (parking_prices_id) REFERENCES parking_prices (id)
);

 
/*///////////////// Logs//////////////// */
CREATE TABLE user_logs (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    action_type VARCHAR(50) NOT NULL, 
    description TEXT, 
    action_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
 
CREATE TABLE vehicle_logs (
    id SERIAL PRIMARY KEY,
    vehicle_id INT NOT NULL,
    action_type VARCHAR(50) NOT NULL, 
    description TEXT, -- Detalhes sobre a ação
    action_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);
 
 
/* Procedure*/
CREATE OR REPLACE FUNCTION log_user_actions()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO user_logs (user_id, action_type, description, action_time)
    VALUES (NEW.id, TG_OP, 'Registro de ação do usuário', CURRENT_TIMESTAMP);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
 
 
-- Trigger para logar inserções de usuários
CREATE TRIGGER user_after_insert
AFTER INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION log_user_actions();
 
-- Trigger para logar atualizações de usuários
CREATE TRIGGER user_after_update
AFTER UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION log_user_actions();
 
 
 
SELECT
    tgname AS trigger_name,
    tgenabled AS enabled,
    tgtype AS trigger_type,
    CASE
        WHEN tgenabled = 'O' THEN 'Enabled'
        WHEN tgenabled = 'D' THEN 'Disabled'
        ELSE 'Unknown'
    END AS enabled_status,
    pg_catalog.pg_get_triggerdef(t.oid) AS trigger_definition
FROM
    pg_catalog.pg_trigger t
    JOIN pg_catalog.pg_class c ON t.tgrelid = c.oid
    JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
WHERE
    n.nspname NOT IN ('pg_catalog', 'information_schema')
ORDER BY
    trigger_name;


-- Media registro meses
CREATE OR REPLACE FUNCTION calculate_average_records_per_month()
RETURNS TABLE(year INT, month INT, average_records_per_month FLOAT) AS
$$
BEGIN
    RETURN QUERY
    SELECT
        EXTRACT(YEAR FROM entry_time)::INT AS year,  -- Cast para INT
        EXTRACT(MONTH FROM entry_time)::INT AS month,  -- Cast para INT
        COUNT(*)::FLOAT / (EXTRACT(DAY FROM (date_trunc('MONTH', CURRENT_DATE) + INTERVAL '1 MONTH' - INTERVAL '1 day')))
    FROM
        parking_records
    GROUP BY
        EXTRACT(YEAR FROM entry_time),
        EXTRACT(MONTH FROM entry_time)
    ORDER BY
        year,
        month;
END;
$$ LANGUAGE plpgsql;

--Seed

INSERT INTO  users
("name", email, "password", "role")
VALUES('Administrador', 'admin@gmail.com', '$2a$10$1HVGUdEQ1y4DX10DRUKDK.z/i59K9iXQeQty8Pwc8znnuSoua2Ta6', 'ADMIN'::public."role_type");

INSERT INTO users
("name", email, "password", "role", enabled)
VALUES('Anderson Barbosa', 'anderson.barbosa@gmail.com', '$2a$10$tUo05DzvN7VIYTq1uiRIjOM22QoCFwU/OdYeB9lzS/H38NV/fT4Pi', 'CLIENT'::public."role_type", true);

INSERT INTO users (name, email, password, role) VALUES
('João Silva', 'joao.silva@example.com', 'senha123', 'CLIENT'),
('Maria Oliveira', 'maria.oliveira@example.com', 'senha456', 'CLIENT'),
('Carlos Santos', 'carlos.santos@example.com', 'senha789', 'CLIENT'),
('Ana Pereira', 'ana.pereira@example.com', 'senha321', 'CLIENT'),
('Ricardo Gomes', 'ricardo.gomes@example.com', 'senha654', 'CLIENT'),
('Fernanda Lima','fernanda.lima@example.com','fernanda','CLIENT'),
('Lucas Almeida','lucas.almeida@example.com','lucas','CLIENT'),
('Tatiane Rocha', 'tatiane.rocha@example.com','tatiane','CLIENT');



INSERT INTO color (name) VALUES
('Preto'),
('Branco'),
('Prata'),
('Vermelho'),
('Azul'),
('Verde'),
('Amarelo'),
('Laranja'),
('Cinza'),
('Roxo'),
('Dourado'),
('Marrom'),
('Bege'),
('Turquesa'),
('Vinho');

INSERT INTO make_car (name) VALUES
('Toyota'),
('Ford'),
('Chevrolet'),
('Volkswagen'),
('Honda'),
('Nissan'),
('Hyundai'),
('Kia'),
('BMW'),
('Mercedes-Benz'),
('Audi'),
('Subaru'),
('Mazda'),
('Lexus'),
('Fiat');

INSERT INTO vehicles (client_id, make_id, model, plate, year, color_id, enabled) VALUES
(1, 1, 'Corolla', 'ANN2F42', 2020, 1, TRUE),
(2, 2, 'Fiesta', 'POX4G21', 2019, 2, TRUE),
(3, 3, 'Onix', 'GCW9AG5', 2021, 5, TRUE),
(1, 4, 'Gol', 'NOP0Q12', 2022, 3, TRUE),
(2, 5, 'Civic', 'AZP0909', 2020, 4, TRUE),
(4, 6, 'Sentra', 'MNB1C23', 2021, 4, TRUE),  
(5, 7, 'Tucson', 'XYZ2D34', 2022, 3, TRUE),  
(6, 8, 'Kicks', 'ABC3E45', 2021, 5, TRUE),  
(7, 9, 'X1', 'FGH4I56', 2023, 1, TRUE),      
(8, 10, 'Civic', 'JKL5M67', 2020, 2, TRUE);  

INSERT INTO parkings
(id, "name")
VALUES(1, 'Shopping Bandeiras');

INSERT INTO parking_prices (parking_id, fixed_rate, extra_hours_rate, start_date, end_date) 
VALUES (1, 16.00,4.00, CURRENT_TIMESTAMP, NULL);

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(4, 1, '2024-09-05 19:00:00.000','2024-09-05 23:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time,exit_time)
VALUES(5, 1, '2024-09-10 08:00:00.000','2024-09-10 15:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(2, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(1, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(1, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(1, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(1, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(1, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(1, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

-- 

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(3, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(3, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(3, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(3, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(3, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(3, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

-- 
INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(4, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(4, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(4, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(4, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(4, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(4, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

-- 
INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(6, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(6, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(6, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(6, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(6, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(6, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

-- 
INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(7, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(7, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(7, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(7, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(7, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(7, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

-- 

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(8, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(8, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(8, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(8, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(8, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(8, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

-- 
INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(9, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(9, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(9, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(9, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(9, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(9, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

-- 
INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(10, 1, '2024-09-20 10:00:00.000','2024-09-20 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(10, 1, '2024-09-21 10:00:00.000','2024-09-21 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(10, 1, '2024-09-22 10:00:00.000','2024-09-22 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(10, 1, '2024-09-23 10:00:00.000','2024-09-23 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(10, 1, '2024-09-24 10:00:00.000','2024-09-24 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time, exit_time)
VALUES(10, 1, '2024-09-25 10:00:00.000','2024-09-25 14:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time )
VALUES(2, 1, '2024-10-24 08:00:00.000');

INSERT INTO parking_records
(vehicles_id, parking_id, entry_time)
VALUES(5, 1, '2024-10-24 10:00:00.000');




INSERT INTO payments (parking_records_id, parking_prices_id, payment_date, amount, pix_code, url_qrcode) 
VALUES (1,1,'2024-09-22 10:00:00', '50.00', 'pix_code_1', 'url_pagamento_1');

INSERT INTO payments (parking_records_id, parking_prices_id, payment_date, amount, pix_code, url_qrcode) 
VALUES (2,1,'2024-09-10 14:50:00', '28.00', 'pix_code_2', 'http://127.0.0.1:5000/static/qrcodes/pixqrcode_2_20240910145000.png');

INSERT INTO payments (parking_records_id, parking_prices_id, payment_date, amount, pix_code, url_qrcode) 
VALUES (3,1,'2024-09-22 13:47:00', '16.00', 'pix_code_3', 'http://127.0.0.1:5000/static/qrcodes/pixqrcode_2_20240922134700.png');