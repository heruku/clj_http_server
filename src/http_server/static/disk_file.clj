(ns http_server.static.disk_file
  (use [http_server.static.file]
       [clojure.java.io])
  (import (java.nio.file Files Paths)
          (java.net URI))) 

(defrecord DiskFile [path]
  File
  (in-stream [this]
    (clojure.java.io/input-stream (:path this)))

  (out-stream [this]
    (clojure.java.io/output-stream (:path this)))

  (content-type [this] 
    (Files/probeContentType (Paths/get (URI. (str "file://" (:path this))))))

  (length [this] 
    (.length (file (:path this)))))
