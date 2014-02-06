--create user for the eventcapture apps
CREATE ROLE eventcaptureuser LOGIN
  ENCRYPTED PASSWORD 'md59781e8b2674aa82664bae96006fc0c32'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;

--create event database
CREATE DATABASE event
  WITH OWNER = eventcaptureuser
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;
