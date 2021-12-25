(ns chess.integration.filesystem.setup
  (:require [clojure.java.io :as io]))

(defn add-game [pgn-text file-name directory & subdirectories]
  (let [file
        (apply
         io/file
         (concat (conj subdirectories directory) [file-name]))]
    (io/make-parents file)
    (spit file pgn-text)))

(defn add-file [contents file-name directory]
  (let [file (io/file directory file-name)]
    (spit file contents)))
