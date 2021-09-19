(ns chess.routes
  (:require
   [hiccup.page :as page]
   [clojure.string :as str]
   [java-time]
   )
  (:import java.io.File))

(declare
 find-events file-names events as-event event-name event-date
 links link-text)

(defn homepage []
  (page/html5
   [:body
    [:a {:href "/events"} "Events"]]))

(defn events-page [events-directory]
  (let [events
        (reverse
         (sort-by
          :date
          (find-events events-directory)))]

    (page/html5
     [:body (links events)])))

(defn find-events [events-directory]
  (let [directories
        (file-names
         (.listFiles
          (new File events-directory)))]
    (events directories)))

(defn events [directories]
  (map as-event directories))

(defn as-event [directory]
  (let [parts
        (str/split directory #"-")]

    {:name (event-name (drop-last 3 parts))
     :date (event-date (take-last 3 parts))}))

(defn file-names [files]
  (map (fn [file] (.getName file)) files))

(defn event-name [name-parts]
  (str/join
   " "
   (map str/capitalize name-parts)))

(defn event-date [date-parts]
  (apply
   java-time/local-date
   (map
    (fn [string] (Integer/parseInt string))
    date-parts)))

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
