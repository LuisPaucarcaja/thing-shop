-- Limpieza de tablas (para evitar conflictos entre tests)
TRUNCATE TABLE product_media RESTART IDENTITY CASCADE;
TRUNCATE TABLE product_categories RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;
TRUNCATE TABLE categories RESTART IDENTITY CASCADE;
TRUNCATE TABLE brands RESTART IDENTITY CASCADE;

-- Insertar Brand
INSERT INTO brands (id, name) VALUES (1, 'Adidas');

-- Insertar Category
INSERT INTO categories (id, name, parent_id) VALUES (1, 'Shoes', NULL);

-- Insertar Product
INSERT INTO products (id, name, description, price, brand_id, discount, qualification, created_at, updated_at)
VALUES (1, 'UltraBoost', 'Running shoes', 120.00, 1, NULL, NULL, NOW(), NOW());

-- Relacionar Product con Category
INSERT INTO product_categories (product_id, category_id) VALUES (1, 1);

-- Insertar ProductMedia
INSERT INTO product_media (id, url, type, is_primary, product_id, visual_variant_id)
VALUES (1, 'http://example.com/ultraboost.jpg', 'IMAGE', TRUE, 1, NULL);
