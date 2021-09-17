(ns chess.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [hiccup.page :as page]
   )
  (:gen-class))

(defn start-app [port join?]
  (run-jetty

   (fn [request]
     {:status 200
      :body
      (page/html5
       [:body
        [:a {:href "/events"} "Events"]])
      })
   {:port port :join? join?}))

(defn -main
  [& args])
