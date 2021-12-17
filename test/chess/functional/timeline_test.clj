(ns chess.functional.timeline-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :as string]
   [ring.mock.request :as mock]
   [chess.core :refer :all]
   [chess.routes :as routes]
   [chess.filesystem :refer :all]
   [chess.setup :refer :all]
   [java-time])
  (:import org.jsoup.Jsoup))

(declare page dates games white black result third)

(def games-directory (create-directory))

(deftest the-timeline-lists-games-by-date-descending
  (add-game
   (string/join
    "\n"
    ["[Event \"Harrow Swiss\"]"
     "[Date \"2021.10.05\"]"
     "[White \"Nicky Chorley\"]"
     "[Black \"David Walker\"]"
     "[Result \"0-1\"]"
     "1. d4 d5 2. c4 c6 *"])
   "chorley-walker.pgn"
   games-directory)

  (add-game
   (string/join
    "\n"
    ["[Event \"Harrow Swiss\"]"
     "[Date \"2021.10.12\"]"
     "[White \"Patrick Sartain\"]"
     "[Black \"Nicky Chorley\"]"
     "[Result \"1-0\"]"
     "1. e4 e6 2. d4 d5 *"])
   "sartain-chorley.pgn"
   games-directory)

  (add-game
   (string/join
    "\n"
    ["[Event \"Harrow Swiss\"]"
     "[Date \"2021.10.01\"]"
     "[White \"James Lyons\"]"
     "[Black \"Nicky Chorley\"]"
     "[Result \"1/2-1/2\"]"
     "1. d4 d5 2. e4 e6*"])
   "lyons-chorley.pgn"
   games-directory)

  (let [timeline-handler
        (routes/timeline
         (partial games-in games-directory))

        timeline (page
                  (timeline-handler (mock/request :get "/timeline")))]

    (is (= "12 October 2021" (first (dates timeline))))

    (is (= "Patrick Sartain" (white (first (games timeline)))))
    (is (= "1-0" (result (first (games timeline)))))
    (is (= "Nicky Chorley" (black (first (games timeline)))))

    (is (= "5 October 2021" (second (dates timeline))))

    (is (= "Nicky Chorley" (white (second (games timeline)))))
    (is (= "0-1" (result (second (games timeline)))))
    (is (= "David Walker" (black (second (games timeline)))))

    (is (= "1 October 2021" (third (dates timeline))))

    (is (= "James Lyons" (white (third (games timeline)))))
    (is (= "1/2-1/2" (result (third (games timeline)))))
    (is (= "Nicky Chorley" (black (third (games timeline)))))))

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

(defn- third [collection]
  (nth collection 2))
