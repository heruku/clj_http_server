(ns http_server.static.get_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.get]
       [http_server.static.memory_file])
  (:import http_server.static.memory_file.MemoryFile))

(def files {"/file1" (MemoryFile. "file1 content" "text/plain")
            "/file2" (MemoryFile. "file2 content" "text/plain")
            "/big-file" (MemoryFile. (apply str (take 10000 (repeat "a"))) "text/plain")
            "/image-file.jpg" (MemoryFile. "image-content" "image/jpg")})

(defn do-get-path [path]
  (do-get {:path path} files))

(describe "status code"
  (it "returns 200 OK for file1"
    (should= 200 (status-of (do-get-path "/file1"))))

  (it "returns 200 OK for file2"
    (should= 200 (status-of (do-get-path "/file2")))))

(describe "content"
  (it "returns contents of file1"
    (should= (to-byte-seq "file1 content") (seq (body-of (do-get-path "/file1")))))

  (it "returns contents of file2"
    (should= (to-byte-seq "file2 content") (seq (body-of (do-get-path "/file2"))))))

(describe "content length header"
  (it "returns content length of small file"
      (should= 13 (get (headers-of (do-get-path "/file1")) "Content-Length")))

  (it "returns content length of big file"
      (should= 10000 (get (headers-of (do-get-path "/big-file")) "Content-Length"))))

(describe "content type header"
  (it "returns content type"
      (should= "image/jpg" (get (headers-of (do-get-path "/image-file.jpg")) "Content-Type"))))
