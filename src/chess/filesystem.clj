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
 make-path)

(defn games-in [directory]
  (let [games (find-games directory)]
    (sort-by :date java-time/after? games)))

(defn- find-games [root-directory]
  (defn find-games-in [directory]
    (let [files (.listFiles (io/file directory))]
      (filter (fn [game] (not (nil? game)))
              (flatten
               (map
                (fn [file]
                  (if (pgn? file)
                    (let [game (parse-game file)]
                      (if (nil? game)
                        nil
                        (assoc
                         (parse-game file)
                         :path (make-path file root-directory))))
                    (find-games-in (.getAbsolutePath file))))
                files)))))
  (find-games-in root-directory))

(defn- parse-game [file]
  (try
    (let [holder (new PgnHolder (.getAbsolutePath file))]
      (.loadPgn holder)
      (let [game (first (.getGames holder))]
        {:white (.getName (.getWhitePlayer game))
         :black (.getName (.getBlackPlayer game))
         :result (parse-result (.getResult game))
         :date (parse-date (.getDate game))
         :round (.getNumber (.getRound game))
         }))

    (catch RuntimeException e nil)))

(defn- parse-result [result]
  (cond
    (= result GameResult/WHITE_WON) :white-won
    (= result GameResult/BLACK_WON) :black-won
    (= result GameResult/DRAW) :draw))

(defn- parse-date [date]
  (java-time/local-date "yyyy.MM.dd" date))

(defn- pgn? [file]
  (and
   (.isFile file)
   (string/ends-with? (.getName file) ".pgn")))

(defn- make-path [file root-directory]
  (-> file
      (.getAbsolutePath)
      (string/replace root-directory "")
      (string/replace ".pgn" "")))
