(ns chess.setup
  (:require [clojure.java.io :as io])
  (:import
   java.nio.file.Files
   java.nio.file.attribute.PosixFilePermission
   java.nio.file.attribute.PosixFilePermissions
   java.nio.file.Path
   java.io.File))

(declare posix-file-permissions)

(defn create-directory []
  (let [path
        (Files/createTempDirectory
         "chess"
         (posix-file-permissions))]
    (str path)))

(defn create-event [events-directory event]
  (Files/createDirectory
   (Path/of events-directory (into-array [event]))
   (posix-file-permissions)))

(defn posix-file-permissions []
  (into-array
   [(PosixFilePermissions/asFileAttribute
     (into #{} (PosixFilePermission/values)))]))

(defn there-are-no-events [events-directory]
  (run!
   (fn [file] (.delete file))
   (.listFiles (new File events-directory))))


(defn add-game [pgn-text file-name directory & subdirectories]
  (let [file
        (apply
         io/file
         (concat (conj subdirectories directory) [file-name]))]
    (io/make-parents file)
    (spit file pgn-text)))
