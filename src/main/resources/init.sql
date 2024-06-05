--CREATE DATABASE afisha;

DROP SCHEMA IF EXISTS public;

CREATE SCHEMA IF NOT EXISTS application;

CREATE TABLE IF NOT EXISTS application.event_type (id serial primary key, name varchar(100));

INSERT INTO application.event_type values (1, 'museum'), (2, 'cinema'), (3, 'theater') ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS application.place (id serial primary key, name varchar(100), address varchar(100), city varchar(100));

CREATE TABLE IF NOT EXISTS application.event (id serial primary key,
								name varchar(100),
								event_type_id int REFERENCES application.event_type(id),
								event_date varchar(100),
								place_id int REFERENCES application.place(id));

CREATE TABLE IF NOT EXISTS application.ticket (id serial primary key,
								event_id int REFERENCES application.event(id),
								client_email varchar(100),
								price numeric(7, 2),
								is_selled bool default false);







