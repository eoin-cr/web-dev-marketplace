#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE TABLE product (
    productid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50),
    description VARCHAR(100),
    price DECIMAL(10, 2),
    hidden BOOLEAN
    );

    CREATE TABLE customer (
    customerid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(50),
    password VARCHAR(50),
    hash VARCHAR(70),
    isadmin BOOLEAN
    );

    CREATE TABLE purchase (
    purchaseid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customerid INT,
    purchasetime TIMESTAMP,
    FOREIGN KEY (customerid) REFERENCES customer(customerid)
    );

    CREATE TABLE purchaseproduct (
    purchaseproductid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    purchaseid INT,
    productid INT,
    quantity INT,
    FOREIGN KEY (purchaseid) REFERENCES purchase(purchaseid),
    FOREIGN KEY (productid) REFERENCES product(productid)
    );

    INSERT INTO product (name, description, price, hidden)
    VALUES
        ('Samsung Galaxy S22 Ultra', 'The latest flagship smartphone from Samsung with advanced camera features', 1300.00, false),
        ('Sony PlayStation 6', 'Next-generation gaming console with 8K graphics support and VR capabilities', 500.00, false),
        ('DJI Phantom 5 Drone', 'Professional-grade drone with 8K camera and obstacle avoidance technology', 1200.00, false),
        ('Tesla Model Y', 'Electric SUV with long-range battery and autopilot features', 45000.00, false),
        ('MacBook Pro 2024', 'Powerful laptop with M2 chip and OLED display', 2000.00, false),
        ('Nintendo Switch Pro', 'Upgraded version of the popular gaming console with 4K support', 400.00, false),
        ('Canon EOS R5', 'High-resolution mirrorless camera with 8K video recording capability', 3500.00, false),
        ('Bose QuietComfort 45', 'Premium noise-canceling headphones with 24-hour battery life', 300.00, false),
        ('Fender American Ultra Stratocaster', 'Professional-grade electric guitar with noiseless pickups and modern features', 1800.00, false),
        ('Peloton Bike+', 'Interactive exercise bike with live and on-demand fitness classes', 2500.00, false);

EOSQL
