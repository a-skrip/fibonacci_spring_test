-- add column price
ALTER TABLE services
ADD COLUMN price INTEGER NOT NULL DEFAULT 0;

-- set price
UPDATE services SET price = 400 WHERE id = 'SOLARIUM';
UPDATE services SET price = 200 WHERE id = 'POOL';
UPDATE services SET price = 0 WHERE id = 'SAUNA';
UPDATE services SET price = 1000 WHERE id = 'CRYOSAUNA';
UPDATE services SET price = 500 WHERE id = 'CROSSFIT';