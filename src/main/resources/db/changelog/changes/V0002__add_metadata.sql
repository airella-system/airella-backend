--liquibase formatted sql

--changeset dybcj:1600604063811-1
ALTER TABLE sensor ADD name VARCHAR(255);

--changeset dybcj:1600604063811-2
ALTER TABLE statistic ADD name VARCHAR(255);
