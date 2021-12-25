(ns chess.integration.filesystem.setup
  (:require [clojure.java.io :as io]))

(defn add-file [contents file-name directory]
  (let [file (io/file directory file-name)]
    (spit file contents)))
