(ns chess.core-test
  (:require [clojure.test :refer :all]
            [chess.core :refer :all]
            [chess.page :refer :all]))

(defn setup [test]
  (start-app {:port 9400 :join? false})
  (test))

(use-fixtures :once setup)

(deftest the-homepage-links-to-the-events-page
  (let [link (first (links (page "http://localhost:9400/")))]
    (is
     (=
      (.text link) "Events"))

    (is
     (= (url link) "/events"))))
