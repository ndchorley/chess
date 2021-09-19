(ns chess.setup
  (:import
   java.nio.file.Files
   java.nio.file.attribute.PosixFilePermission
   java.nio.file.attribute.PosixFilePermissions
   java.nio.file.Path))

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
