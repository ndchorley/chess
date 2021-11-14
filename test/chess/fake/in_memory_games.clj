(ns chess.fake.in-memory-games
  (:require [java-time]))

(defn get-games [games]
  (sort-by :date java-time/after? games))

