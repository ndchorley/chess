(ns chess.core-test
  (:require [clojure.test :refer :all]
            [chess.core :refer :all])
  (:import org.jsoup.Jsoup))

(defn setup [test]
  (start-app {:port 9400 :join? false})
  (test))

(use-fixtures :once setup)

(declare page links url)

(deftest the-homepage-links-to-the-events-page
  (let [link (first (links (page "http://localhost:9400/")))]
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
