TRUNCATE TABLE shipping_addresses, shipping_districts, shipping_cities RESTART IDENTITY CASCADE;

INSERT INTO shipping_cities (id, name, active) VALUES (1, 'Lima', true);
INSERT INTO shipping_cities (id, name, active) VALUES (2, 'Arequipa', true);

INSERT INTO shipping_districts (id, name, shipping_price, active, shipping_city_id)
VALUES (1, 'Miraflores', 10.50, true, 1);
INSERT INTO shipping_districts (id, name, shipping_price, active, shipping_city_id)
VALUES (2, 'San Isidro', 12.00, true, 1);
INSERT INTO shipping_districts (id, name, shipping_price, active, shipping_city_id)
VALUES (3, 'Cercado', 8.00, true, 2);
