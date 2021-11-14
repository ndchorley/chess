(ns chess.fake.in-memory-games-test
  (:require
   [chess.contracts.games]
   [chess.fake.in-memory-games :refer :all]
   [java-time]))

(chess.contracts.games/games-are-returned-by-date-descending-from
 (partial
  get-games
  [{:date (java-time/local-date 1960 8 4)
    :white "Petrosian" :black "Unzicker"}
   {:date (java-time/local-date 2016 11 30)
    :white "Carlsen" :black "Karjakin"}]))
