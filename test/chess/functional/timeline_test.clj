(ns chess.functional.timeline-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :as string]
   [ring.mock.request :as mock]
   [chess.core :refer :all]
   [chess.routes :as routes]
   [chess.fake.in-memory-games :refer :all]
   [java-time])
  (:import org.jsoup.Jsoup))

(declare page dates games white black result)

(deftest the-timeline-lists-games-by-date-descending
  (let [timeline-handler
        (routes/timeline
         (partial
          get-games
           #{
             {:white "Nicky Chorley"
              :black "David Walker"
              :result "0-1"
              :date (java-time/local-date 2021 10 5)}

             {:white "Patrick Sartain"
              :black "Nicky Chorley"
              :result "1-0"
              :date (java-time/local-date 2021 10 12)}}))

        timeline (page
                  (timeline-handler (mock/request :get "/timeline")))]

    (is (= "12 October 2021" (first (dates timeline))))
    (is (= "Patrick Sartain" (white (first (games timeline)))))
    (is (= "1-0" (result (first (games timeline)))))
    (is (= "Nicky Chorley" (black (first (games timeline)))))

    (is (= "5 October 2021" (second (dates timeline))))
    (is (= "Nicky Chorley" (white (second (games timeline)))))
    (is (= "0-1" (result (second (games timeline)))))
    (is (= "David Walker" (black (second (games timeline)))))))

(defn- page [response]
  (Jsoup/parse (response :body)))

(defn- dates [timeline]
  (map
   (fn [element] (.text element))
   (.select timeline ".date")))

(defn- games [timeline]
  (.select timeline ".game"))

(defn- white [game]
  (.text (.selectFirst game ".white")))

(defn- black [game]
  (.text (.selectFirst game ".black")))

(defn- result [game]
  (.text (.selectFirst game ".result")))
