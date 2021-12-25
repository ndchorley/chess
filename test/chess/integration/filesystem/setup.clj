(ns chess.integration.filesystem.setup
  (:require [clojure.java.io :as io])
  (:import
   java.nio.file.Files
   java.nio.file.attribute.PosixFilePermission
   java.nio.file.attribute.PosixFilePermissions))

(declare posix-file-permissions)

(defn create-directory []
  (let [path
        (Files/createTempDirectory
         "chess"
         (posix-file-permissions))]
    (str path)))

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

(defn- posix-file-permissions []
  (into-array
   [(PosixFilePermissions/asFileAttribute
     (into #{} (PosixFilePermission/values)))]))
