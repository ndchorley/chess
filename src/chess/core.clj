(ns chess.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [compojure.core :refer :all]
   [chess.routes :refer :all])
  (:gen-class))

(defn create-app [config]
  (routes
   (GET "/" [] (homepage))
   (GET "/events" []
        (events-page (config :events-directory)))))

(defn start-app [config]
  (run-jetty (create-app config) config))

(defn -main [& args]
  (start-app
   {:port (Integer/parseInt (System/getenv "PORT"))
    :join? true
    :events-directory (System/getenv "EVENTS_DIRECTORY")}))
