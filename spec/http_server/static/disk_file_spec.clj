(ns http_server.static.disk_file_spec
  (use [speclj.core]
       [http_server.static.disk_file]
       [http_server.static.file])
  (import (http_server.static.disk_file DiskFile)))

(def text-file
  (DiskFile. "/Users/ukutaht/Desktop/code/http_server/spec/http_server/fixtures/text_file.txt"))

(defn to-byte-seq [string]
  (seq (bytes (byte-array (map byte string)))))

(describe "disk file"
  (it "reads the contents of a text file"
    (should= (to-byte-seq "text content\n") (seq (contents text-file))))

  (it "reads the length of a text file"
    (should= (count "text content\n") (length text-file)))

  (it "can replace it's contents"
    (let [new-file (replace-contents text-file "new content\n")]
      (should= (to-byte-seq "new content\n") (seq (contents text-file))) )))
