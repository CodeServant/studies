CREATE DATABASE samochody;

-- Table: Cars mysql create
REATE TABLE Cars (
    idCar int NOT NULL,
    typ varchar(100) NOT NULL,
    marka varchar(100) NOT NULL,
    rokProd int NOT NULL,
    spalanie decimal(4,1) NOT NULL,
    CONSTRAINT Cars_pk PRIMARY KEY (idCar)
);

-- Table: Cars oracle create
CREATE TABLE Cars (
    idCar integer  NOT NULL,
    typ varchar2(100)  NOT NULL,
    marka varchar2(100)  NOT NULL,
    rokProd integer  NOT NULL,
    spalanie number(4,1)  NOT NULL,
    CONSTRAINT Cars_pk PRIMARY KEY (idCar)
) ;

INSERT INTO cars (idCar, typ, marka, rokProd, spalanie) VALUES (1, 'osobowy', 'volkswagen', 2020, 6.5);
INSERT INTO cars (idCar, typ, marka, rokProd, spalanie) VALUES (2, 'ciężarowy', 'skana', 2016, 5.5);
INSERT INTO cars (idCar, typ, marka, rokProd, spalanie) VALUES (3, 'F1', 'aston martin', 2015, 4.3);
INSERT INTO cars (idCar, typ, marka, rokProd, spalanie) VALUES (4, 'dostawczy', 'łada', 2012, 12.5);
INSERT INTO cars (idCar, typ, marka, rokProd, spalanie) VALUES (5, 'dostawczy', 'Składa', 2011, 1.5);
INSERT INTO cars (idCar, typ, marka, rokProd, spalanie) VALUES (6, 'osobowy', 'peaugot', 2000, 4.5);
