-- Создание таблицы partner
CREATE TABLE partner (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         country VARCHAR(100),
                         contact_info VARCHAR(255),
                         tax_number VARCHAR(50),
                         type VARCHAR(20) NOT NULL
);

-- Создание таблицы user (в кавычках, так как user - зарезервированное слово)
CREATE TABLE "user" (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        role VARCHAR(20) NOT NULL,
                        email VARCHAR(100),
                        create_at TIMESTAMP
);

-- Создание таблицы warehouse
CREATE TABLE warehouse (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           address TEXT,
                           manager VARCHAR(100)
);

-- Создание таблицы document (без внешнего ключа на request)
CREATE TABLE document (
                          id BIGSERIAL PRIMARY KEY,
                          file_as_array_of_bytes BYTEA,
                          file_path VARCHAR(255),
                          doc_type VARCHAR(50),
                          created_at TIMESTAMP,
                          partner_id BIGINT REFERENCES partner(id)
);

-- Создание таблицы request
CREATE TABLE request (
                         id BIGSERIAL PRIMARY KEY,  -- Изменено с INT на BIGINT для согласованности
                         partner_id BIGINT NOT NULL REFERENCES partner(id),
                         document_id BIGINT UNIQUE REFERENCES document(id),
                         quantity INTEGER NOT NULL,
                         type VARCHAR(50) NOT NULL,
                         status VARCHAR(20) NOT NULL DEFAULT 'NEW',
                         created_at TIMESTAMP
);

-- Добавление внешнего ключа request_id в document
ALTER TABLE document ADD COLUMN request_id BIGINT UNIQUE;
ALTER TABLE document ADD CONSTRAINT fk_document_request
    FOREIGN KEY (request_id) REFERENCES request(id);

-- Создание таблицы product
CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         product_code VARCHAR(50) UNIQUE,
                         origin_country VARCHAR(100),
                         unit_of_measure VARCHAR(20),
                         base_price NUMERIC(19,4),
                         request_id BIGINT REFERENCES request(id)
);

-- Создание таблицы warehouse_product с составным ключом
CREATE TABLE warehouse_product (
                                   product_id BIGINT NOT NULL REFERENCES product(id),
                                   warehouse_id INTEGER NOT NULL REFERENCES warehouse(id),
                                   quantity INTEGER NOT NULL,
                                   updated_at TIMESTAMP,
                                   PRIMARY KEY (product_id, warehouse_id)
);
