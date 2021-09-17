(ns chess.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [hiccup.page :as page])
  (:gen-class))

(defn events [request]
  {:status 200
   :body
   (page/html5
    [:body
     [:a {:href "/events"} "Events"]])
   })

(defn start-app [port join?]
  (run-jetty
   events
   {:port port :join? join?}))

(defn -main
  [& args])
