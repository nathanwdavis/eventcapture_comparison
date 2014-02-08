-- -*- sql-product: postgres -*-

DO $$ BEGIN
    IF NOT EXISTS (SELECT * FROM pg_tables WHERE tablename = 'eventlog') THEN
        -- create the eventlog table
        CREATE TABLE eventlog (
            id serial PRIMARY KEY,
            timeutc timestamp with time zone NOT NULL default (now() at time zone 'utc'),
            event_type varchar(200) NOT NULL CHECK (event_type <> ''),
            ext_ref varchar(200),
            user_ref varchar(200),
            data json
        );

        CREATE INDEX eventlog_timeutc_type_user_idx
            ON eventlog (timeutc ASC,
               event_type NULLS LAST,
               user_ref NULLS LAST);
    END IF;
END; $$;

-- a function to help in doing inserts
CREATE OR REPLACE FUNCTION insert_eventlog
    (
        et varchar(200),
        er varchar(200),
        ur varchar(200),
        d varchar
    ) RETURNS int AS $$

    INSERT INTO eventlog (event_type, ext_ref, user_ref, data)
        VALUES (et, er, ur, d::json)
    RETURNING id;
$$ LANGUAGE SQL;
