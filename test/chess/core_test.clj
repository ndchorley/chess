(ns chess.core-test
  (:require
   [clojure.test :refer :all]
   [chess.core :refer :all]
   [chess.page :refer :all]
   [chess.setup :refer :all])
  (:import java.io.File))

(def events-directory (create-directory))

(defn setup [test]
  (start-app
   {:port 9400 :join? false :events-directory events-directory
    :games-directory events-directory})
  (test))

(use-fixtures :once setup)

(deftest the-timeline-is-reachable
  (is
   (=
    (title (page "http://localhost:9400/timeline"))
    "Timeline")))
