(ns http_server.static.memory_file
  (:use [http_server.static.file])
  (:import (java.io StringBufferInputStream
                    ByteArrayOutputStream)))

(defrecord MemoryFile [contents content-type]
  File
  (in-stream [this] (StringBufferInputStream. (:contents this)))
  (out-stream [this] (ByteArrayOutputStream.))
  (content-type [this] (:content-type this))
  (length [this] (count (:contents this))))
