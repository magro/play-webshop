# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "brands" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"description" VARCHAR);
create table "categories" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"description" VARCHAR);
create table "product_attributes" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"product_id" BIGINT NOT NULL,"name" VARCHAR NOT NULL,"value" VARCHAR NOT NULL);
create table "products" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"brand_id" INTEGER NOT NULL,"category_id" INTEGER NOT NULL,"name" VARCHAR NOT NULL,"snippet" VARCHAR NOT NULL,"description" VARCHAR NOT NULL,"image_url" VARCHAR NOT NULL,"image_urls" VARCHAR);
alter table "product_attributes" add constraint "product_fk" foreign key("product_id") references "products"("id") on update NO ACTION on delete NO ACTION;
alter table "products" add constraint "category_fk" foreign key("category_id") references "categories"("id") on update NO ACTION on delete NO ACTION;
alter table "products" add constraint "brand_fk" foreign key("brand_id") references "brands"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "product_attributes" drop constraint "product_fk";
alter table "products" drop constraint "category_fk";
alter table "products" drop constraint "brand_fk";
drop table "brands";
drop table "categories";
drop table "product_attributes";
drop table "products";