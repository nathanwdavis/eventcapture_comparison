eventcapture comparison
=======================

Comparison of Python, Go, and Clojure implementations of a simple HTTP service to capture and store generic event data.

Since the goal is just to compare, the requirements are simplified and the implementations are mostly done with one main source file.

Basic Requirements:
-------------------

- Should accept a POST request with the following params:
   1. event_type
   2. ext_ref (optional) - an application-defined reference to some other system of record
   3. user_ref (optional) - an  application-defined reference to the user that performed the action or event
   4. data - a string of valid json that contains metadata specific to the event
   5. timeutc (optional) - a timestamp in RFC3339 format. This should only be used for log-replay or backfilling scenarios.

- Should return a status 204 response (no content)

- Should set the timeutc column to the current time UTC (unless timeutc is passed in on the request). It should preferrably rely on the DB to do this.

- Should use the provided SQL function to perform the insert (mainly just to test for good support for SQL functions)

- Should respond with some constant string when requesting the root ('/') so that it could be PINGd for up/down status

Web Frameworks/Libraries
------------------------

Clojure -> Compojure

Golang -> Martini

Python -> Flask

Setup Instructions
------------------

1. Have a Postgres v9.3 or higher running on you localhost.
2. Execute 'init.sql' against your local Postgres server (this will create a user and an `event` database)
3. Execute 'create_eventlog_table.sql' in the context of the `event` database.
