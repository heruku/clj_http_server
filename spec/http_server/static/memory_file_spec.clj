(ns http_server.static.memory_file_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.memory_file]
       [http_server.static.file])
  (import (http_server.static.memory_file MemoryFile)))

(def text-file
  (MemoryFile. "text content\n" "text/plain"))

(describe "memory file"
  (it "reads the contents of a text file"
    (should= (to-byte-seq "text content\n") (seq (contents text-file))))

  (it "reads a range of bytes from text file"
    (should= (to-byte-seq "text ") (seq (content-range text-file 0 4))))

  (it "reads the length of a text file"
    (should= (count "text content\n") (length text-file)))

  (it "reads the length of a text file"
    (let [new-file (replace-contents text-file "new content\n")]
      (should= (to-byte-seq "new content\n") (seq (contents new-file))))))
