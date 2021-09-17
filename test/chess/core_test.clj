(ns chess.core-test
  (:require [clojure.test :refer :all]
            [chess.core :refer :all])
  (:import org.jsoup.Jsoup))

(defn setup [test]
  (start-app 9400 false)
  (test))

(use-fixtures :once setup)

(declare page links url)

(deftest the-user-sees-a-link-to-the-events-page
  (let [link (links (page "http://localhost:9400/"))]
    (is
     (=
      (.text link) "Events"))

    (is
     (= (url link) "/events"))))


(defn page [url]
  (.get (Jsoup/connect url)))

(defn links [page]
  (.select page "a"))

(defn url [link]
  (.attr link "href"))
