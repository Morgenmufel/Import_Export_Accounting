CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       email VARCHAR(100),
                       created_at TIMESTAMP
);

CREATE TABLE partners (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          country VARCHAR(100),
                          contact_info TEXT,
                          tax_number VARCHAR(50),
                          type VARCHAR(20)
);

CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          product_code VARCHAR(100),
                          origin_country VARCHAR(100),
                          unit_of_measure VARCHAR(50),
                          base_price NUMERIC(10,2)
);

CREATE TABLE warehouses (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255),
                            address TEXT,
                            manager VARCHAR(255)
);

CREATE TABLE warehouse_product (
                                   product_id INT NOT NULL,
                                   warehouse_id INT NOT NULL,
                                   quantity INT NOT NULL,
                                   updated_at TIMESTAMP,
                                   PRIMARY KEY (product_id, warehouse_id),
                                   FOREIGN KEY (product_id) REFERENCES products(id),
                                   FOREIGN KEY (warehouse_id) REFERENCES warehouses(id)
);

CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              date DATE,
                              type VARCHAR(10),
                              partner_id INT REFERENCES partners(id),
                              user_id INT REFERENCES users(id),
                              warehouse_id INT REFERENCES warehouses(id),
                              total_value NUMERIC(12, 2)
);

CREATE TABLE transaction_product (
                                     transaction_id INT NOT NULL,
                                     product_id INT NOT NULL,
                                     quantity INT NOT NULL,
                                     unit_price NUMERIC(10,2),
                                     PRIMARY KEY (transaction_id, product_id),
                                     FOREIGN KEY (transaction_id) REFERENCES transactions(id),
                                     FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE documents (
                           id SERIAL PRIMARY KEY,
                           transaction_id INT UNIQUE REFERENCES transactions(id),
                           file_path VARCHAR(255),
                           doc_type VARCHAR(100),
                           created_at TIMESTAMP
);
