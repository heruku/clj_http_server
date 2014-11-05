(ns http_server.static.disk_file
  (use [http_server.static.file]
       [clojure.java.io])
  (import (java.nio.file Files Paths)
          (java.net URI))) 

(defrecord DiskFile [path]
  File

  (contents [this] 
    (with-open [stream (clojure.java.io/input-stream (:path this))]
      (let [contents (byte-array (length this))]
        (.read stream contents)
        contents)))

  (content-range [this start end]
    (with-open [stream (clojure.java.io/input-stream (:path this))]
      (let [length (inc (- end start))
            contents (byte-array length)]
        (.read stream contents start length)
        contents)))

  (content-type [this] 
    (Files/probeContentType (Paths/get (URI. (str "file://" (:path this))))))

  (length [this] 
    (.length (file (:path this))))

  (replace-contents [this new-contents] 
    (with-open [writer (clojure.java.io/writer (:path this))]
      (.write writer new-contents)
      (.write writer "\n"))
    this))
