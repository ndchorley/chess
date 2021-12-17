(ns chess.routes
  (:require
   [compojure.core :refer :all]
   [hiccup.page :as page]
   [clojure.string :as str]
   [java-time]))

(declare links link-text result-text)

(defn homepage []
  (page/html5
   [:body
    [:a {:href "/events"} "Events"]]))

(defn events-page [events]
  (page/html5
   [:body (links (events))]))


(defn timeline [games]
  (GET
   "/timeline"
   []
   (page/html5
    [:title "Timeline"]
    [:body
     [:div [:h1 "Timeline"]]
     (map
      (fn [game]
        [:div
         [:div
          {:class "date"}
          (java-time/format "d MMMM uuuu" (game :date))]

         [:div
          {:class "game"}
          [:div {:class "white"} (game :white)]
          [:div
           {:class "result"}
           (result-text (game :result))]
          [:div {:class "black"} (game :black)]]])
      (games))])))

(defn- links [events]
  (map
   (fn [event]
     [:a
      {:href (str "/events/" (event :slug))}
      (link-text event)])
   events))

(defn- link-text [event]
  (str/join
   ", "
   [(event :name)
    (java-time/format
     "d MMMM uuuu"
     (event :date))]))

(defn- result-text [result]
  (cond
    (= result :white-won) "1-0"
    (= result :black-won) "0-1"
    (= result :draw) "1/2-1/2"))
