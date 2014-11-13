(ns http_server.static.file_spec
  (:use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.memory_file]
       [http_server.static.file])
  (:import (java.io StringBufferInputStream)
          (http_server.static.memory_file MemoryFile)))

(def file (MemoryFile. "file contents" "text/plain"))

(describe "file functions"
  (it "reads a range from a file"
    (should= (to-byte-seq "file") 
             (seq (content-range file 0 3))))

  (it "reads a range from a file"
    (should= (to-byte-seq "nts") 
             (seq (content-range file 10 12))))

  (it "reads all contents from a file"
    (should= (to-byte-seq "file contents") 
             (seq (contents file))))

  (it "replaces all contents of a file"
    (should= (to-byte-seq "new contents\n") 
             (seq (.toByteArray (replace-contents file "new contents"))))))
