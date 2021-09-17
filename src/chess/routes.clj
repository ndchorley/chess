(ns chess.routes
  (:require [hiccup.page :as page]))

(defn homepage []
  (page/html5
   [:body
    [:a {:href "/events"} "Events"]]))
