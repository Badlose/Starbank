--liquibase formatted sql

--changeset isolomin:1
CREATE TABLE if NOT EXISTS dynamic_recommendation (
    id BIGINT generated by default as identity primary key,
    name CHARACTER VARYING(255) not null,
    product_id UUID not null,
    text TEXT not null
);

CREATE TABLE if NOT EXISTS rule (
    id BIGINT generated by default as identity primary key,
    arguments CHARACTER VARYING[] not null,
    negate BOOLEAN not null,
    query CHARACTER VARYING(255) not null,
    recommendation_id BIGINT REFERENCES dynamic_recommendation (id) not null
);

--changeset isolomin:2
CREATE TABLE if NOT EXISTS statistic (
    id BIGINT generated by default as identity primary key,
    recommendation_id BIGINT not null,
    counter INTEGER
)
