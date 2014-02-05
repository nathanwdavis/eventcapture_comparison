(ns eventcapture.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route])
  (:require [clojure.data.json :as json])
  (:require [clojure.string :as strings]))

(defn now [] (new java.util.Date))

(defn handle-event-post [event-type ext-ref user-ref data]
  {:pre [(string? event-type) 
         (not (strings/blank? event-type))
         (string? data)
         (json/read-str data)]}
  (str event-type "|" ext-ref "|" user-ref "|" (json/read-str data)))

(defroutes app-routes
  (GET "/" [] "It works!")
  (POST "/capture" [event-type ext-ref user-ref data]
        (handle-event-post event-type ext-ref user-ref data))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
