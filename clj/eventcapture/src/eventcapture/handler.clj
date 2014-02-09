(ns eventcapture.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [clojure.string :as strings]
            [clojure.java.jdbc :as sql])
  (:import com.jolbox.bonecp.BoneCPDataSource))

(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//localhost:5432/event"
              ; Any additional keys are passed to the driver
              ; as driver-specific properties.
              :user "eventcaptureuser"
              :password "postgres"})

(defn pool
  [spec]
  (let [partitions 3
        min-pool 2
        max-pool 100
        cpds (doto (BoneCPDataSource.)
              (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
              (.setUsername (:user spec))
              (.setPassword (:password spec))
              (.setMinConnectionsPerPartition (inc (int (/ min-pool partitions))))
              (.setMaxConnectionsPerPartition (inc (int (/ max-pool partitions))))
              (.setPartitionCount partitions)
              (.setStatisticsEnabled true)
              ;; test connections every 25 mins (default is 240):
              (.setIdleConnectionTestPeriodInMinutes 25)
              ;; allow connections to be idle for 3 hours (default is 60 minutes):
              (.setIdleMaxAgeInMinutes (* 3 60))
              ;; consult the BoneCP documentation for your database:
              (.setConnectionTestStatement "/* ping *\\/ SELECT 1"))]
    {:datasource cpds}))
(def pooled-db (delay (pool db-spec)))

(defn db-connection [] @pooled-db)

(defn- insert-event [event-map]
  (sql/with-db-connection [conn (db-connection)]
    (sql/query conn ["select insert_eventlog(?, ?, ?, ?)"
                     (:event_type event-map)
                     (:ext_ref event-map)
                     (:user_ref event-map)
                     (:data event-map)]
               :row-fn :id
               :result-set-fn first))
  "")

(defn handle-event-post [event-type ext-ref user-ref data]
  {:pre [(string? event-type)
         (not (strings/blank? event-type))
         (string? data)
         (json/read-str data)]}
  (insert-event {:event_type event-type
                 :ext_ref ext-ref
                 :user_ref user-ref
                 :data data}))

(defroutes app-routes
  (GET "/" [] "It works!")
  (POST "/capture" [event_type ext_ref user_ref data]
        (handle-event-post event_type ext_ref user_ref data))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
