(ns http_server.static.memory_file
  (use [http_server.static.file]))

(defrecord MemoryFile [contents content-type]
  File
  (contents [this] (bytes (byte-array (map byte (:contents this)))))
  (content-type [this] (:content-type this))
  (length [this] (count (:contents this))))
