CREATE TABLE if not exists yb_sales (
    pkid BIGSERIAL PRIMARY KEY,
    sale_id VARCHAR(36),
    item_id VARCHAR(10),
    quantity INT,
    sale_time TIMESTAMP WITHOUT TIME ZONE,
    store_id VARCHAR(10),
    sale_price NUMERIC(10, 2),
    product_name VARCHAR(255),
    product_category VARCHAR(50),
    state_code VARCHAR(2)
);

-- Insert some data
INSERT INTO yb_sales (sale_id, item_id, quantity, sale_time, store_id, sale_price, product_name, product_category, state_code)
VALUES ('a4a70900-24e1-11df-8924-001ff3591711', '123', 1, '2024-03-25 06:11:23', '001', 99.99, 'Gadget', 'Electronics', 'NY');

-- Insert some data with the current timestamp
INSERT INTO yb_sales (sale_id, item_id, quantity, sale_time, store_id, sale_price, product_name, product_category, state_code)
VALUES ('a4a70900-24e1-11df-8924-001ff3591711', '123', 1, NOW(), '001', 31.39, 'Gadget', 'Tools & Home Improvement', 'CA');



select * from yb_sales limit 10;