(ns chess.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [compojure.core :refer :all]
   [chess.routes :refer :all]
   [chess.filesystem :as filesystem])
  (:gen-class))

(defn create-app [config]
  (routes
   (timeline
    (partial
     filesystem/games-in
     (config :games-directory)))))

(defn start-app [config]
  (run-jetty (create-app config) config))

(defn -main [& args]
  (start-app
   {:port (Integer/parseInt (System/getenv "PORT"))
    :join? true
    :events-directory (System/getenv "EVENTS_DIRECTORY")
    :games-directory (System/getenv "GAMES_DIRECTORY")}))
