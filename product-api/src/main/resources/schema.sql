INSERT INTO CATEGORY (id, description) VALUES (1, 'Comic Books');
INSERT INTO CATEGORY (id, description) VALUES (2, 'Movies');
INSERT INTO CATEGORY (id, description) VALUES (3, 'Books');

INSERT INTO supplier (id, name) VALUES (1, 'Panini Comics');
INSERT INTO supplier (id, name) VALUES (2, 'Panini Comics');

INSERT INTO product (id, name, fk_supplier, fk_category, quantity) VALUES (1, 'Crise nas Infinitas Terras', 1, 1, 10);
INSERT INTO product (id, name, fk_supplier, fk_category, quantity) VALUES (2, 'Interstellar', 2, 2, 5);