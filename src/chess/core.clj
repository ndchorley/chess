(ns chess.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [compojure.core :refer :all]
   [chess.routes :refer :all])
  (:gen-class))

(defroutes app
  (GET "/" [] (homepage)))

(defn start-app [config]
  (run-jetty app config))

(defn -main [& args]
  (start-app
   {:port
    (Integer/parseInt
     (System/getenv "PORT"))
    :join?
    true}))
