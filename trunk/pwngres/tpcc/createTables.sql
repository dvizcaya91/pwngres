CREATE TABLE district 
(
D_ID integer primary key, 
D_W_ID integer, 
D_TAX integer, 
D_NEXT_O_ID integer
);

CREATE TABLE customer
(
C_ID integer,
C_D_ID integer,
C_W_ID integer,
C_DISCOUNT integer
);

CREATE TABLE neworder
(
NO_O_ID integer primary key,
NO_D_ID integer,
NO_W_ID integer
);

CREATE TABLE orders
(
O_ID integer,
O_D_ID integer,
O_W_ID integer,
O_C_ID integer,
O_OL_CNT integer
);

CREATE TABLE orderline
(
OL_O_ID integer primary key,
OL_D_ID integer,
OL_W_ID integer,
OL_NUMBER integer,
OL_I_ID integer,
OL_SUPPLY_W_ID integer,
OL_QUANTITY integer,
OL_AMOUNT integer
);

CREATE TABLE item
(
I_ID integer primary key,
I_PRICE integer
);

CREATE TABLE stock
(
S_I_ID integer primary key,
S_W_ID integer,
S_QUANTITY integer,
S_ORDER_CNT integer
);