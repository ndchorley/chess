(ns chess.functional.timeline-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :as string]
   [ring.mock.request :as mock]
   [chess.core :refer :all]
   [chess.routes :as routes]
   [chess.filesystem :refer :all]
   [chess.setup :refer :all]
   [chess.game-builder :refer :all]
   [java-time])
  (:import org.jsoup.Jsoup))

(declare
 page dates games white black result third)

(deftest the-timeline-lists-games-by-date-descending
  (let [games-directory (create-directory)]
    (add-game
     (-> a-game
         (played-on "2021.10.05")
         (between-white "Nicky Chorley")
         (and-black "David Walker")
         (with-result "0-1")
         as-pgn)
     "chorley-walker.pgn"
     games-directory)

    (add-game
     (-> a-game
         (played-on "2021.10.12")
         (between-white "Patrick Sartain")
         (and-black "Nicky Chorley")
         (with-result "1-0")
         as-pgn)
     "sartain-chorley.pgn"
     games-directory)

    (add-game
     (-> a-game
         (played-on "2021.10.01")
         (between-white "James Lyons")
         (and-black "Nicky Chorley")
         (with-result "1/2-1/2")
         as-pgn)
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
      (is (= "Nicky Chorley" (black (third (games timeline))))))))

(deftest
  games-played-on-the-same-day-are-grouped-and-listed-by-round-descending

  (let [games-directory (create-directory)
        event-date "2021.08.14"]
    (add-game
     (-> a-game
         (played-on event-date)
         (in-round "1")
         (between-white "Nicky Chorley")
         (and-black "David Everitt")
         as-pgn)
     "chorley-everitt.pgn"
     games-directory)

    (add-game
     (-> a-game
         (played-on event-date)
         (in-round "2")
         (between-white "James Baxter")
         (and-black "Nicky Chorley")
         as-pgn)
     "baxter-chorley.pgn"
     games-directory)

    (add-game
     (-> a-game
         (played-on event-date)
         (in-round "3")
         (between-white "Nicky Chorley")
         (and-black "Charles Dabbs")
         as-pgn)
     "chorley-dabbs.pgn"
     games-directory)

    (let [timeline-handler
          (routes/timeline
           (partial games-in games-directory))

          timeline (page
                    (timeline-handler (mock/request :get "/timeline")))]

      (is (= 1 (count (dates timeline))))

      (is (= "Nicky Chorley" (white (first (games timeline)))))
      (is (= "Charles Dabbs" (black (first (games timeline)))))

      (is (= "James Baxter" (white (second (games timeline)))))
      (is (= "Nicky Chorley" (black (second (games timeline)))))

      (is (= "Nicky Chorley" (white (third (games timeline)))))
      (is (= "David Everitt" (black (third (games timeline))))))))

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
