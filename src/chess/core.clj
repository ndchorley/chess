(ns chess.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [compojure.core :refer :all]
   [chess.routes :refer :all]
   [chess.events :refer [find-events]]
   [chess.filesystem :as filesystem])
  (:gen-class))

(defn create-app [config]
  (routes
   (GET "/" [] (homepage))
   (timeline (partial filesystem/games-in (config :games-directory)))
   (GET "/events" []
        (events-page
         (partial
          find-events
          (config :events-directory))))))

(defn start-app [config]
  (run-jetty (create-app config) config))

(defn -main [& args]
  (run!
   #(println (str % ": " (System/getenv %)))
   ["EVENTS_DIRECTORY" "GAMES_DIRECTORY"])

  (start-app
   {:port (Integer/parseInt (System/getenv "PORT"))
    :join? true
    :events-directory (System/getenv "EVENTS_DIRECTORY")
    :games-directory (System/getenv "GAMES_DIRECTORY")}))
