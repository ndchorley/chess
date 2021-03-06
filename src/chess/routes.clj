(ns chess.routes
  (:require
   [compojure.core :refer :all]
   [hiccup.page :as page]
   [clojure.string :as str]
   [java-time]))

(declare result-text date-sections game-rows)

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
     [:div
      {:class "container"}

      (let [games-by-date (group-by :date (games))]
        (date-sections games-by-date))]])))

(defn- date-sections [games-by-date]
  (let [dates (sort java-time/after? (keys games-by-date))]
    (map
     (fn [date]
       [:div
        [:div
         {:class "row date"}
         [:h4 (java-time/format "d MMMM uuuu" date)]]

        (game-rows (games-by-date date))])
     dates)))

(defn game-rows [games]
  (let [sorted-by-round (sort-by :round > games)]
    (map
     (fn [game]
       [:div
        {:class "row game"}
        [:div {:class "col white"} (game :white)]
        [:div
         {:class "col result"}
         (result-text (game :result))]
        [:div {:class "col black"} (game :black)]
        [:div
         {:class "col"}
         [:a
          {:href (game :path)}
          "View game"]]])
     sorted-by-round)))

(defn- result-text [result]
  (cond
    (= result :white-won) "1-0"
    (= result :black-won) "0-1"
    (= result :draw) "1/2-1/2"))
