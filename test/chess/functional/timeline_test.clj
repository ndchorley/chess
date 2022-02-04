(ns chess.functional.timeline-test
  (:require
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [ring.mock.request :as mock]
   [chess.routes :as routes]
   [chess.lib :refer :all]
   [chess.filesystem :refer :all]
   [chess.functional.page :refer :all]
   [chess.integration.filesystem.setup :refer :all]
   [chess.game-builder :refer :all])

  (:import org.apache.commons.io.FileUtils))

(def games-directory (create-directory))

(def timeline-route
  (routes/timeline (partial games-in games-directory)))

(defn empty-games-directory [test]
  (FileUtils/cleanDirectory (io/as-file games-directory))
  (test))

(use-fixtures :each empty-games-directory)

(deftest the-timeline-lists-games-by-date-descending
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

  (let [timeline
        (page
         (timeline-route
          (mock/request :get "/timeline")))]

    (is (= (first (dates timeline)) "12 October 2021"))

    (is (= (white (first (games timeline))) "Patrick Sartain"))
    (is (= (result (first (games timeline))) "1-0"))
    (is (= (black (first (games timeline))) "Nicky Chorley"))

    (is (= (second (dates timeline)) "5 October 2021"))

    (is (= (white (second (games timeline))) "Nicky Chorley"))
    (is (= (result (second (games timeline))) "0-1"))
    (is (= (black (second (games timeline))) "David Walker"))

    (is (= (third (dates timeline)) "1 October 2021"))

    (is (= (white (third (games timeline))) "James Lyons"))
    (is (= (result (third (games timeline))) "1/2-1/2"))
    (is (= (black (third (games timeline))) "Nicky Chorley"))))

(deftest
  games-played-on-the-same-day-are-grouped-and-listed-by-round-descending

  (let [event-date "2021.08.14"]
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

    (let [timeline
          (page
           (timeline-route
            (mock/request :get "/timeline")))]

      (is (= (count (dates timeline)) 1))

      (is (= (white (first (games timeline))) "Nicky Chorley"))
      (is (= (black (first (games timeline))) "Charles Dabbs"))

      (is (= (white (second (games timeline))) "James Baxter"))
      (is (= (black (second (games timeline))) "Nicky Chorley"))

      (is (= (white (third (games timeline))) "Nicky Chorley"))
      (is (= (black (third (games timeline))) "David Everitt")))))

(deftest a-game-has-a-link-to-view-it
  (add-game
   (-> a-game as-pgn)
   "chorley-goldsmith.pgn"
   games-directory
   "harrow-swiss"
   "2021")

  (let [timeline
        (page
         (timeline-route
          (mock/request :get "/timeline")))]

    (is (= (text (link (first (games timeline)))) "View game"))))
