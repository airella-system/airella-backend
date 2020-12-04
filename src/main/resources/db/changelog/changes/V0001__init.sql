--liquibase formatted sql

--changeset jakubdybczak:1606345404506-1
CREATE TABLE address (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, city VARCHAR(255), country VARCHAR(255), number VARCHAR(255), street VARCHAR(255), CONSTRAINT "addressPK" PRIMARY KEY (id));

--changeset jakubdybczak:1606345404506-2
CREATE TABLE client (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, refresh_token VARCHAR(255), CONSTRAINT "clientPK" PRIMARY KEY (id));

--changeset jakubdybczak:1606345404506-3
CREATE TABLE client_role (id BIGINT NOT NULL, roles VARCHAR(255));

--changeset jakubdybczak:1606345404506-4
CREATE TABLE measurement (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, timestamp TIMESTAMP WITHOUT TIME ZONE, value FLOAT8 NOT NULL, sensor_db_id BIGINT, CONSTRAINT "measurementPK" PRIMARY KEY (id));

--changeset jakubdybczak:1606345404506-5
CREATE TABLE multiple_value_enum_statistic (chart_type VARCHAR(255), db_id BIGINT NOT NULL, latest_statistic_value_db_id BIGINT, CONSTRAINT "multiple_value_enum_statisticPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-6
CREATE TABLE multiple_value_float_statistic (chart_type VARCHAR(255), metric VARCHAR(255), db_id BIGINT NOT NULL, latest_statistic_value_db_id BIGINT, CONSTRAINT "multiple_value_float_statisticPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-7
CREATE TABLE one_value_string_statistic (db_id BIGINT NOT NULL, CONSTRAINT "one_value_string_statisticPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-8
CREATE TABLE sensor (db_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, id VARCHAR(255), name VARCHAR(255), type VARCHAR(255), latest_measurement_id BIGINT, station_db_id BIGINT, CONSTRAINT "sensorPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-9
CREATE TABLE station (db_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, id VARCHAR(255), latitude FLOAT8 NOT NULL, longitude FLOAT8 NOT NULL, name VARCHAR(255), station_visibility VARCHAR(255), address_id BIGINT, owner_id BIGINT, station_client_id BIGINT, CONSTRAINT "stationPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-10
CREATE TABLE station_client (id BIGINT NOT NULL, CONSTRAINT "station_clientPK" PRIMARY KEY (id));

--changeset jakubdybczak:1606345404506-11
CREATE TABLE statistic (db_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, id VARCHAR(255), name VARCHAR(255), statistic_privacy_mode VARCHAR(255), statistic_type VARCHAR(255), station_db_id BIGINT, CONSTRAINT "statisticPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-12
CREATE TABLE statistic_enum_definition (db_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, id VARCHAR(255), name VARCHAR(255), statistic_db_id BIGINT, CONSTRAINT "statistic_enum_definitionPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-13
CREATE TABLE statistic_value (db_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, timestamp TIMESTAMP WITHOUT TIME ZONE, statistic_db_id BIGINT, CONSTRAINT "statistic_valuePK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-14
CREATE TABLE statistic_value_float (value FLOAT8 NOT NULL, db_id BIGINT NOT NULL, CONSTRAINT "statistic_value_floatPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-15
CREATE TABLE statistic_value_string (value VARCHAR(255), db_id BIGINT NOT NULL, CONSTRAINT "statistic_value_stringPK" PRIMARY KEY (db_id));

--changeset jakubdybczak:1606345404506-16
CREATE TABLE user_client (email VARCHAR(255), password_hash VARCHAR(255), station_registration_token VARCHAR(255), id BIGINT NOT NULL, CONSTRAINT "user_clientPK" PRIMARY KEY (id));

--changeset jakubdybczak:1606345404506-17
CREATE TABLE user_client_stub (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, activate_string VARCHAR(255), email VARCHAR(255), password_hash VARCHAR(255), CONSTRAINT "user_client_stubPK" PRIMARY KEY (id));

--changeset jakubdybczak:1606345404506-18
ALTER TABLE client ADD CONSTRAINT UC_CLIENTREFRESH_TOKEN_COL UNIQUE (refresh_token);

--changeset jakubdybczak:1606345404506-19
ALTER TABLE station ADD CONSTRAINT UC_STATIONID_COL UNIQUE (id);

--changeset jakubdybczak:1606345404506-20
ALTER TABLE user_client ADD CONSTRAINT UC_USER_CLIENTEMAIL_COL UNIQUE (email);

--changeset jakubdybczak:1606345404506-21
ALTER TABLE user_client_stub ADD CONSTRAINT UC_USER_CLIENT_STUBEMAIL_COL UNIQUE (email);

--changeset jakubdybczak:1606345404506-22
ALTER TABLE statistic ADD CONSTRAINT "UK84hkw6bkpo2lbbekbq0wy1k3a" UNIQUE (station_db_id, id);

--changeset jakubdybczak:1606345404506-23
ALTER TABLE sensor ADD CONSTRAINT "UKe1xtqt35tarjrusx7y1rtx3vs" UNIQUE (station_db_id, id);

--changeset jakubdybczak:1606345404506-24
CREATE INDEX INDX_0 ON measurement(timestamp);

--changeset jakubdybczak:1606345404506-25
CREATE INDEX INDX_1 ON statistic_value(timestamp);

--changeset jakubdybczak:1606345404506-26
ALTER TABLE statistic ADD CONSTRAINT "FK20np2syxcj308c7xcygxo717w" FOREIGN KEY (station_db_id) REFERENCES station (db_id);

--changeset jakubdybczak:1606345404506-27
ALTER TABLE station ADD CONSTRAINT "FK4qcdqbyfa4u1tpxhhskj6cvek" FOREIGN KEY (station_client_id) REFERENCES station_client (id);

--changeset jakubdybczak:1606345404506-28
ALTER TABLE station ADD CONSTRAINT "FK52qw87l9ttkr3nf32phsvqe6f" FOREIGN KEY (address_id) REFERENCES address (id);

--changeset jakubdybczak:1606345404506-29
ALTER TABLE statistic_enum_definition ADD CONSTRAINT "FK7qbe70fcgmwig4sr92bwxnxg7" FOREIGN KEY (statistic_db_id) REFERENCES multiple_value_enum_statistic (db_id);

--changeset jakubdybczak:1606345404506-30
ALTER TABLE sensor ADD CONSTRAINT "FK88wiltqsbte20tvhvd60siapm" FOREIGN KEY (latest_measurement_id) REFERENCES measurement (id);

--changeset jakubdybczak:1606345404506-31
ALTER TABLE statistic_value ADD CONSTRAINT "FKa3gf9a84cbhck12sops84lw8p" FOREIGN KEY (statistic_db_id) REFERENCES statistic (db_id);

--changeset jakubdybczak:1606345404506-32
ALTER TABLE measurement ADD CONSTRAINT "FKa5ehlq2tq9buy8sjxtuk7el85" FOREIGN KEY (sensor_db_id) REFERENCES sensor (db_id);

--changeset jakubdybczak:1606345404506-33
ALTER TABLE sensor ADD CONSTRAINT "FKbdp56mm8l0qu4ujmubmroqvbh" FOREIGN KEY (station_db_id) REFERENCES station (db_id);

--changeset jakubdybczak:1606345404506-34
ALTER TABLE station_client ADD CONSTRAINT "FKbkmmakw2c5qmjuh3qh22lrjsy" FOREIGN KEY (id) REFERENCES client (id);

--changeset jakubdybczak:1606345404506-35
ALTER TABLE station ADD CONSTRAINT "FKck6b3rbj8cq68osm122c726ns" FOREIGN KEY (owner_id) REFERENCES user_client (id);

--changeset jakubdybczak:1606345404506-36
ALTER TABLE user_client ADD CONSTRAINT "FKgdrjsb0tj0tk2yb6iibukfh4e" FOREIGN KEY (id) REFERENCES client (id);

--changeset jakubdybczak:1606345404506-37
ALTER TABLE one_value_string_statistic ADD CONSTRAINT "FKgucneisxwvef60x0kuquug3yg" FOREIGN KEY (db_id) REFERENCES statistic (db_id);

--changeset jakubdybczak:1606345404506-38
ALTER TABLE multiple_value_float_statistic ADD CONSTRAINT "FKir5wtfem894fakxkmwfm7j9a4" FOREIGN KEY (db_id) REFERENCES statistic (db_id);

--changeset jakubdybczak:1606345404506-39
ALTER TABLE statistic_value_float ADD CONSTRAINT "FKjclxircilxjxiaii4v8cdg81p" FOREIGN KEY (db_id) REFERENCES statistic_value (db_id);

--changeset jakubdybczak:1606345404506-40
ALTER TABLE client_role ADD CONSTRAINT "FKlc5c40lwdi77xfj5jlrwpt4uc" FOREIGN KEY (id) REFERENCES client (id);

--changeset jakubdybczak:1606345404506-41
ALTER TABLE multiple_value_enum_statistic ADD CONSTRAINT "FKlw263aitple2grh1irb5vyw7h" FOREIGN KEY (db_id) REFERENCES statistic (db_id);

--changeset jakubdybczak:1606345404506-42
ALTER TABLE multiple_value_float_statistic ADD CONSTRAINT "FKqptrnt37cdtsjkk6315kc579j" FOREIGN KEY (latest_statistic_value_db_id) REFERENCES statistic_value (db_id);

--changeset jakubdybczak:1606345404506-43
ALTER TABLE multiple_value_enum_statistic ADD CONSTRAINT "FKquv3vcedmv2bd0e9p9syab95p" FOREIGN KEY (latest_statistic_value_db_id) REFERENCES statistic_value (db_id);

--changeset jakubdybczak:1606345404506-44
ALTER TABLE statistic_value_string ADD CONSTRAINT "FKr3s34xtbay7fcq89xbim08syv" FOREIGN KEY (db_id) REFERENCES statistic_value (db_id);

