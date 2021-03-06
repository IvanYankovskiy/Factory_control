--liquibase formatted sql

--changeset application:1 dbms:postgresql
--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_type where typname='device_type' and typtype='e';
create type device_type AS enum(
    'EXTRUDER',
    'PRESS'
);

--changeset application:2 dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:f SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'device');
CREATE TABLE IF NOT EXISTS device (
    id          INTEGER PRIMARY KEY,
    token       VARCHAR(100) unique not null,
    name        VARCHAR(50) not null,
    device_type device_type not NULL ,
    description VARCHAR(100)
);

--changeset application:3 dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.sequences where sequence_name='device_id_sequence' and sequence_schema='public';
CREATE SEQUENCE device_id_sequence;