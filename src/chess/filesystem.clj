(ns chess.filesystem
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [java-time])
  (:import
   com.github.bhlangonijr.chesslib.pgn.PgnHolder
   com.github.bhlangonijr.chesslib.game.GameResult))

(declare
 find-games parse-game parse-result parse-date pgn?
 make-path valid? files-in queue-of-files-in queue-of)

(defn games-in [directory]
  (let [games (find-games directory)]
    (sort-by :date java-time/after? games)))

(defn- find-games [directory]
  (loop [
         files (queue-of-files-in directory)
         directories (queue-of directory)
         games []]
    (if (zero? (.size files))
      games

      (let [file (.remove files)]
        (cond
          (.isDirectory file)
          (do
            (.addAll files (files-in file))
            (.add directories file)
            (recur files directories games))

          (pgn? file)
          (let [game (parse-game file directory)]
              (if (nil? game)
                (recur files directories games)

                (recur files directories (conj games game))))

          true (recur files directories games))))))

(defn- queue-of [directory]
  (doto
      (new java.util.concurrent.LinkedBlockingQueue)
      (.add directory)))

(defn- queue-of-files-in [directory]
  (doto
      (new java.util.concurrent.LinkedBlockingQueue)
      (.addAll (files-in directory))))

(defn- files-in [directory]
  (into [] (.listFiles (io/file directory))))

(defn- parse-game [file directory]
  (try
    (let [holder (new PgnHolder (.getAbsolutePath file))]
      (.loadPgn holder)
      (let [game (first (.getGames holder))]
        {:white (.getName (.getWhitePlayer game))
         :black (.getName (.getBlackPlayer game))
         :result (parse-result (.getResult game))
         :date (parse-date (.getDate game))
         :round (.getNumber (.getRound game))
         :path (make-path file directory)}))

    (catch RuntimeException e nil)))

(defn- parse-result [result]
  (cond
    (= result GameResult/WHITE_WON) :white-won
    (= result GameResult/BLACK_WON) :black-won
    (= result GameResult/DRAW) :draw))

(defn- parse-date [date]
  (java-time/local-date "yyyy.MM.dd" date))

(defn- valid? [game] (not (nil? game)))

(defn- pgn? [file]
  (and
   (.isFile file)
   (string/ends-with? (.getName file) ".pgn")))

(defn- make-path [file root-directory]
  (-> file
      (.getAbsolutePath)
      (string/replace root-directory "")
      (string/replace ".pgn" "")))
