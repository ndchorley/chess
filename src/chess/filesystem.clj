(ns chess.filesystem
  (:require
   [clojure.java.io :as io]
   [java-time])
  (:import
   com.github.bhlangonijr.chesslib.pgn.PgnHolder
   com.github.bhlangonijr.chesslib.game.GameResult))

(declare find-games parse-game parse-result parse-date)

(defn games-in [directory]
  (flatten (find-games directory)))

(defn- find-games [directory]
    (let [files (.listFiles (io/file directory))]
      (map
       (fn [file]
         (if (.isFile file)
           (parse-game file)
           (find-games (.getAbsolutePath file))))
       files)))

(defn- parse-game [file]
  (let [holder (new PgnHolder (.getAbsolutePath file))]
    (.loadPgn holder)
    (let [game (first (.getGames holder))]
      {:white (.getName (.getWhitePlayer game))
       :black (.getName (.getBlackPlayer game))
       :result (parse-result (.getResult game))
       :date (parse-date (.getDate game))})))

(defn- parse-result [result]
  (cond
    (= result GameResult/WHITE_WON) :white-won
    (= result GameResult/BLACK_WON) :black-won
    (= result GameResult/DRAW) :draw))

(defn parse-date [date]
  (java-time/local-date "yyyy.MM.dd" date))
