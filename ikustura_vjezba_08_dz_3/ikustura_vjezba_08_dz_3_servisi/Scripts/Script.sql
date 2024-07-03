create table kazne (
	rb int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	id int NOT NULL,
	vrijemePocetak bigint NOT NULL,
	vrijemeKraj bigint NOT NULL,
	brzina double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL,
	gpsSirinaRadar double NOT NULL,
	gpsDuzinaRadar double NOT NULL
); 

create table voznje (
	rb int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	id int NOT NULL,
	broj int NOT NULL,
	vrijeme bigint NOT NULL,
	brzina double NOT NULL,
	snaga double NOT NULL,
	struja double NOT NULL,
	visina double NOT NULL,
	gpsBrzina double NOT NULL,
	tempVozila int NOT NULL,
	postotakBaterija int NOT NULL,
	naponBaterija double NOT NULL,
	kapacitetBaterija int NOT NULL,
	tempBaterija int NOT NULL,
	preostaloKm double NOT NULL,
	ukupnoKm double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL
); 

create table pracenevoznje (
	rb int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	id int NOT NULL,
	broj int NOT NULL,
	vrijeme bigint NOT NULL,
	brzina double NOT NULL,
	snaga double NOT NULL,
	struja double NOT NULL,
	visina double NOT NULL,
	gpsBrzina double NOT NULL,
	tempVozila int NOT NULL,
	postotakBaterija int NOT NULL,
	naponBaterija double NOT NULL,
	kapacitetBaterija int NOT NULL,
	tempBaterija int NOT NULL,
	preostaloKm double NOT NULL,
	ukupnoKm double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL
); 


CREATE USER nwtis_3 PASSWORD 'nwtis#3';

CREATE ROLE aplikacija;

GRANT aplikacija TO "nwtis_3";

GRANT SELECT, UPDATE, INSERT ON TABLE kazne TO aplikacija;
GRANT SELECT, UPDATE, INSERT ON TABLE voznje TO aplikacija;
GRANT SELECT, UPDATE, INSERT ON TABLE pracenevoznje TO aplikacija;

SELECT * FROM INFORMATION_SCHEMA.USERS;
SELECT * FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES;

SELECT * FROM kazne;
SELECT * FROM voznje;
SELECT * FROM PRACENEVOZNJE;

DELETE FROM pracenevoznje;
DELETE FROM kazne;
DELETE FROM voznje;


SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, 
kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina FROM pracenevoznje WHERE id = 1;

SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, 
preostaloKm, ukupnoKm, gpsSirina, gpsDuzina FROM pracenevoznje WHERE id = 1 AND vrijeme >= 1708074290218 AND vrijeme <= 1708074314540;

SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, 
preostaloKm, ukupnoKm, gpsSirina, gpsDuzina FROM pracenevoznje WHERE vrijeme >= 1708074290218 AND vrijeme <= 1708074314540;

SELECT id, vrijemePocetak, vrijemeKraj, brzina, gpsSirina, gpsDuzina, gpsSirinaRadar, gpsDuzinaRadar 
FROM kazne WHERE vrijemeKraj >= 1708073817415 AND vrijemeKraj <= 1708073926199;
SELECT id, vrijemePocetak, vrijemeKraj, brzina, gpsSirina, gpsDuzina, gpsSirinaRadar, gpsDuzinaRadar
FROM kazne WHERE rb = 2168;
SELECT rb FROM kazne;
DROP USER nwtis_3;
