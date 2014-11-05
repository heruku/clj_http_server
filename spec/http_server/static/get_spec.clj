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

(defn content-length [response]
  (get (headers-of response) "Content-Length"))

(defn content-type [response]
  (get (headers-of response) "Content-Type"))

(defn body-seq [response]
  (seq (body-of response)))

(defn call-get 
  ([path] (do-get {:path path} files))
  ([path headers] (do-get (merge {:path path} headers) files)))

(describe "status code"
  (it "returns 200 OK for file1"
    (should= 200 (status-of (call-get "/file1"))))

  (it "returns 200 OK for file2"
    (should= 200 (status-of (call-get "/file2")))))

(describe "content"
  (it "returns contents of file1"
    (should= (to-byte-seq "file1 content") (body-seq (call-get "/file1"))))

  (it "returns contents of file2"
    (should= (to-byte-seq "file2 content") (body-seq (call-get "/file2")))))

(describe "content length header"
  (it "returns content length of small file"
      (should= 13 (content-length (call-get "/file1"))))

  (it "returns content length of big file"
      (should= 10000 (content-length (call-get "/big-file")))))

(describe "content type header"
  (it "returns content type for text file"
      (should= "text/plain" (content-type (call-get "/file1"))))

  (it "returns content type"
      (should= "image/jpg" (content-type (call-get "/image-file.jpg"))))) 

(describe "Partial content"
  (it "returns 206 Partial Content if range header is present"
    (should= 206 (status-of (call-get "/file1" {:headers {"Range" "bytes=0-4"}})))) 

  (it "serves n first bytes of the file"
    (should= (to-byte-seq "file1") (body-seq (call-get "/file1" {:headers {"Range" "bytes=0-4"}}))))

  (it "returns the correct content type"
    (should= "text/plain" (content-type (call-get "/file1" {:headers {"Range" "bytes=0-4"}}))))

  (it "returns the number of bytes read in content length header"
     (should= 6 (content-length (call-get "/file1" {:headers {"Range" "bytes=0-5"}})))))  
