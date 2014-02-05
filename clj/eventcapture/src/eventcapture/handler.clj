(ns eventcapture.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn handle-event-post [event-type ext-ref user-ref data]
  (println event-type)
  (str event-type))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/capture" [event-type ext-ref user-ref data]
        (handle-event-post event-type ext-ref user-ref data))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
