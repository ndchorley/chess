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
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     [:link {:href
             "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
             :rel "stylesheet"
             :integrity "sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
             :crossorigin "anonymous"}]
     [:title "Timeline"]]
    [:body
     [:div [:h1 "Timeline"]]
     (map
      (fn [game]
        [:div
         [:div
          {:class "container"}
          [:div
           {:class "row date"}
           [:h4 (java-time/format "d MMMM uuuu" (game :date))]]

          [:div
           {:class "row game"}
           [:div {:class "col white"} (game :white)]
           [:div
            {:class "col result"}
            (result-text (game :result))]
           [:div {:class "col black"} (game :black)]]]])
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
