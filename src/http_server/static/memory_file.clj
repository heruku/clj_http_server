(ns http_server.static.memory_file
  (use [http_server.static.file]))

(defn get-bytes [string] (bytes (byte-array (map byte string))))

(defrecord MemoryFile [contents content-type]
  File
  (contents [this] (get-bytes (:contents this)))
  (content-range [this start end] (get-bytes (subs (:contents this) start (inc end))))
  (content-type [this] (:content-type this))
  (length [this] (count (:contents this)))
  (replace-contents [this new-contents]
    (MemoryFile. new-contents (:content-type this))))
