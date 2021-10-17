(ns chess.events
  (:require
   [clojure.string :as str]
   [java-time])
  (:import java.io.File))

(declare
 file-names events as-event event-name event-date)

(defn find-events [events-directory]
  (let [directories
        (file-names
         (.listFiles
          (new File events-directory)))]
    (reverse
     (sort-by
      :date
      (events directories)))))

(defn events [directories]
  (map as-event directories))

(defn as-event [directory]
  (let [parts
        (str/split directory #"-")]

    {:name (event-name (drop-last 3 parts))
     :date (event-date (take-last 3 parts))
     :slug directory}))

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
