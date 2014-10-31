(ns http_server.static.memory_file_spec
  (use [speclj.core]
       [http_server.static.memory_file]
       [http_server.static.file])
  (import (http_server.static.memory_file MemoryFile)))

(def text-file
  (MemoryFile. "text content\n" "text/plain"))

(defn to-byte-seq [string]
  (seq (bytes (byte-array (map byte string)))))

(describe "disk file"
  (it "reads the contents of a text file"
    (should= (to-byte-seq "text content\n") (seq (contents text-file)))))
