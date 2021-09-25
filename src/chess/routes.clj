(ns chess.routes
  (:require
   [hiccup.page :as page]
   [clojure.string :as str]
   [java-time]
   [chess.events :refer :all])
  (:import java.io.File))

(declare links link-text)

(defn homepage []
  (page/html5
   [:body
    [:a {:href "/events"} "Events"]]))

(defn events-page [events-directory]
  (let [events
        (find-events events-directory)]

    (page/html5
     [:body (links events)])))


(defn links [events]
  (map
   (fn [event] [:a (link-text event)])
   events))

(defn link-text [event]
  (str/join
   ", "
   [(event :name)
    (java-time/format
     "d MMMM uuuu"
     (event :date))]))
