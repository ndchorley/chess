(ns chess.contracts.games
  (:require
   [clojure.test :refer :all]
   [java-time]))

(declare dates adjacent-pairs)

(defn games-are-returned-by-date-descending-from [games]
  (deftest games-are-returned-by-date-descending
    (run!
     (fn [pair]
       (is (java-time/after? (first pair) (second pair))))
     (adjacent-pairs (dates (games))))))

(defn- dates [games] (map :date games))

(defn- adjacent-pairs [dates] (partition 2 1 dates))
