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
   {:port 9400 :join? false :events-directory events-directory})
  (test))

(use-fixtures :once setup)

(deftest the-homepage-links-to-the-events-page
  (let [link (first (links (page "http://localhost:9400/")))]
    (is (= (.text link) "Events"))

    (is (= (url link) "/events"))))

(deftest the-events-page-lists-events-by-date-descending
  (there-are-no-events events-directory)

  (create-event
   events-directory
   "richmond-rapidplay-2019-10-05")
  (create-event
   events-directory
   "golders-green-rapidplay-2021-08-14")

  (let [events
        (text (links (page "http://localhost:9400/events")))]

    (is
     (=
      events
      ["Golders Green Rapidplay, 14 August 2021"
       "Richmond Rapidplay, 5 October 2019"]))))

(deftest the-events-page-links-to-a-page-for-each-event
  (there-are-no-events events-directory)

  (create-event
   events-directory
   "docklands-rapidplay-2021-09-30")

  (let [link (first (links (page "http://localhost:9400/events")))]
    (is (= (url link) "/events/docklands-rapidplay-2021-09-30"))))
