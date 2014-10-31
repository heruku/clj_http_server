(ns http_server.static.disk_file_spec
  (use [speclj.core]
       [http_server.static.disk_file]
       [http_server.static.file])
  (import (http_server.static.disk_file DiskFile)
          (java.util Arrays)))

(def text-file
  (DiskFile. "/Users/ukutaht/Desktop/code/http_server/spec/http_server/fixtures/text_file.txt"))

(defn to-byte-seq [string]
  (seq (bytes (byte-array (map byte string)))))

(describe "disk file"
  (it "reads the contents of a text file"
    (should= (to-byte-seq "text content\n") (seq (contents text-file)))))
