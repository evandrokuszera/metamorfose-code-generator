-- Database: orders_db

-- DROP DATABASE orders_db;

-- CREATE DATABASE orders_db
--     WITH 
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'Portuguese_Brazil.1252'
--     LC_CTYPE = 'Portuguese_Brazil.1252'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1;
	
-- Table: public.orders

-- DROP TABLE public.orders;

CREATE TABLE public.customers
(
    id serial NOT NULL,
    nome character varying(100) COLLATE pg_catalog."default",
    nascimento date,
    renda double precision,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.customers
    OWNER to postgres;

CREATE TABLE public.orders
(
    id serial NOT NULL,
    orderdate date,
    total double precision,
    customerid integer,
    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT customerid_fk FOREIGN KEY (customerid)
        REFERENCES public.customers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public.orders
    OWNER to postgres;
	
-- Table: public.products

-- DROP TABLE public.products;

CREATE TABLE public.products
(
    id serial NOT NULL,
    description character varying COLLATE pg_catalog."default",
    price double precision,
    CONSTRAINT products_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.products
    OWNER to postgres;
	
-- Table: public.orderlines

-- DROP TABLE public.orderlines;

CREATE TABLE public.orderlines
(
    id serial NOT NULL,
    orderlinedate date,
    quantity double precision,
    price double precision,
    orderid integer,
    prodid integer,
    CONSTRAINT orderlines_pkey PRIMARY KEY (id),
    CONSTRAINT fk_orders_id FOREIGN KEY (orderid)
        REFERENCES public.orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_products_id FOREIGN KEY (prodid)
        REFERENCES public.products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public.orderlines
    OWNER to postgres;
    
delete from orderlines;
delete from orders;
delete from customers;
delete from products;

insert into products (description, price) values ('Percy Jackson e o Ladrão de Raios', 47.5);
insert into products (description, price) values ('Percy Jackson e os Olimpianos', 65.4);
insert into products (description, price) values ('Harry Potter I', 55.4);
insert into products (description, price) values ('Harry Potter II', 55.4);
insert into products (description, price) values ('Harry Potter III', 65.4);

insert into customers (nome, nascimento, renda) values ('João Silva', '21/06/1980', 5500.0);
insert into customers (nome, nascimento, renda) values ('Maria Pereira', '05/10/1983', 12500.0);
insert into customers (nome, nascimento, renda) values ('Pedro Rocha', '13/10/1985', 35500.0);

insert into orders (customerid, orderdate, total) values (1, '15-01-2021', 112.90);
insert into orders (customerid, orderdate, total) values (2, '18-02-2021', 176.2);
insert into orders (customerid, orderdate, total) values (3, '24-03-2021', 47.5);
insert into orders (customerid, orderdate, total) values (3, '20-07-2021', 110.8);

insert into orderlines (orderlinedate, quantity, price, orderid, prodid) values ('15-01-2021', 1, 47.5, 1, 1);
insert into orderlines (orderlinedate, quantity, price, orderid, prodid) values ('15-01-2021', 1, 65.4, 1, 2);

insert into orderlines (orderlinedate, quantity, price, orderid, prodid) values ('18-02-2021', 1, 55.4, 2, 3);
insert into orderlines (orderlinedate, quantity, price, orderid, prodid) values ('18-02-2021', 1, 55.4, 2, 4);
insert into orderlines (orderlinedate, quantity, price, orderid, prodid) values ('18-02-2021', 1, 65.4, 2, 5);

insert into orderlines (orderlinedate, quantity, price, orderid, prodid) values ('24-03-2021', 1, 47.5, 3, 1);
insert into orderlines (orderlinedate, quantity, price, orderid, prodid) values ('20-07-2021', 2, 55.4, 4, 3);

 

