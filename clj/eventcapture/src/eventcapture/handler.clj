(ns eventcapture.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [clojure.string :as strings]
            [clojure.java.jdbc :as sql]))

(def db {:classname "org.postgresql.Driver"
         :subprotocol "postgresql"
         :subname "//localhost:5432/event"
         ; Any additional keys are passed to the driver
         ; as driver-specific properties.
         :user "eventcaptureuser"
         :password "postgres"})

(defn now [] (new java.util.Date))

(defn- insert-event [event-map]
  (sql/with-db-connection [conn db]
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
  (POST "/capture" [event-type ext-ref user-ref data]
        (handle-event-post event-type ext-ref user-ref data))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
