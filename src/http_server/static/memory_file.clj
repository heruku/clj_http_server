(ns http_server.static.memory_file
  (use [http_server.static.file]))

(defrecord MemoryFile [contents]
  File
  (contents [this] (:contents this)))
