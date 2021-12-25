(ns chess.end-to-end.app-test
  (:require
   [clojure.test :refer :all]
   [chess.core :refer :all]
   [chess.end-to-end.page :refer :all]
   [chess.integration.filesystem.setup :refer :all]))

(def games-directory (create-directory))

(defn setup [test]
  (start-app
   {:port 9400
    :join? false
    :games-directory games-directory})
  (test))

(use-fixtures :once setup)

(deftest the-timeline-is-reachable
  (is
   (=
    (title (page "http://localhost:9400/timeline"))
    "Timeline")))
