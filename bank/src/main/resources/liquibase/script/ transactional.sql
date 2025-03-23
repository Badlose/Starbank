changelog-master.yaml
-- changeset dmitriy:1

SELECT *
FROM (
    SELECT t.ID AS TRANSACTION_ID, 
           t.USER_ID, 
           t.type AS transaction_type, 
           t.AMOUNT, 
           p.ID AS PRODUCT_ID, 
           p.TYPE AS PRODUCT_TYPE, 
           p.NAME AS PRODUCT_NAME
    FROM TRANSACTIONS t
    JOIN PRODUCTS p ON t.product_id = p.id
);