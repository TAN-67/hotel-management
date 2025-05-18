CREATE DATABASE hotel_management;
USE hotel_management;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYEE') NOT NULL
);

CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE') NOT NULL
);

CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN'),
('employee', 'emp123', 'EMPLOYEE');

INSERT INTO rooms (room_number, type, price, status) VALUES 
('101', 'Single', 50.00, 'AVAILABLE'),
('102', 'Double', 80.00, 'AVAILABLE');