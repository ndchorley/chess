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
 make-path valid? files-and-directories-in
 queue-of only-files only-directories only-pgns
 parse-games
 )

(defn games-in [directory]
  (let [games (find-games directory)]
    (sort-by :date java-time/after? games)))

(defn- find-games [root-directory]
  (loop [directories (queue-of root-directory)
         games []]
    (cond
      (zero? (.size directories)) games

      true
      (let [current-directory (.remove directories)
            files-and-directories
            (files-and-directories-in current-directory)

            parse-games (partial parse-games root-directory)

            files (only-files files-and-directories)
            pgns (only-pgns files)
            games-found (parse-games pgns)
            
            valid-games (filter valid? games-found)

            directories-found
            (only-directories files-and-directories)]
        (do
          (.addAll directories directories-found)
          (recur directories (concat games valid-games)))))))

(defn parse-games [root-directory pgns]
  (map
   (fn [pgn] (parse-game pgn root-directory))
   pgns))

(defn- only-pgns [files] (filter pgn? files))

(defn- only-files [files-and-directories]
  (filter
   (fn [file-or-directory] (.isFile file-or-directory))
   files-and-directories))

(defn- only-directories [files-and-directories]
  (filter
   (fn [file-or-directory] (.isDirectory file-or-directory))
   files-and-directories))

(defn- queue-of [directory]
  (doto
      (new java.util.LinkedList)
      (.add directory)))

(defn- files-and-directories-in [directory]
  (into [] (.listFiles (io/file directory))))

(defn- parse-game [file root-directory]
  (try
    (let [holder (new PgnHolder (.getAbsolutePath file))]
      (.loadPgn holder)
      (let [game (first (.getGames holder))]
        {:white (.getName (.getWhitePlayer game))
         :black (.getName (.getBlackPlayer game))
         :result (parse-result (.getResult game))
         :date (parse-date (.getDate game))
         :round (.getNumber (.getRound game))
         :path (make-path file root-directory)}))

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
