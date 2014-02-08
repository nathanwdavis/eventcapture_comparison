-- -*- sql-product: postgres -*-

DO $$ BEGIN
    --create user for the eventcapture apps
    IF NOT EXISTS (SELECT * FROM pg_catalog.pg_user WHERE usename = 'eventcaptureuser') THEN
        CREATE ROLE eventcaptureuser LOGIN
            ENCRYPTED PASSWORD 'md59781e8b2674aa82664bae96006fc0c32'
            NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
    END IF;
END; $$;

--create event database
CREATE DATABASE event
  WITH OWNER = eventcaptureuser
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;
