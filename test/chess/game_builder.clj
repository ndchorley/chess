(ns chess.game-builder
  (:require
   [clojure.string :as string]))

(def a-game
  {:event "Harrow Swiss"
   :round "1"
   :result "1-0"
   :white "Jennifer Goldsmith"
   :black "Nicky Chorley"
   :moves "1. d4 d5 2. e4 e6 *"})

(defn played-on [game date]
  (assoc game :date date))

(defn in-round [game round]
  (assoc game :round round))

(defn between-white [game white]
  (assoc game :white white))

(defn and-black [game black]
  (assoc game :black black))

(defn with-result [game result]
  (assoc game :result result))

(defn as-pgn [game]
  (string/join
   "\n"
   [(str "[Event \"" (game :event) "\"]")
    (str "[Date \"" (game :date) "\"]")
    (str "[Round \"" (game :round) "\"]")
    (str "[White \"" (game :white) "\"]")
    (str "[Black \"" (game :black) "\"]")
    (str "[Result \"" (game :result) "\"]")
    (game :moves)]))
