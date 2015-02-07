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

(describe "parsing the range header"
  (it "parses range with start and end"
    (should= [0 4] (get-byte-range {:headers {"Range" "bytes=0-4"}} 10)))

  (it "parses range with no end specified"
    (should= [4 9] (get-byte-range {:headers {"Range" "bytes=4-"}} 10)))

  (it "parses range with no start speficied"
    (should= [6 9] (get-byte-range {:headers {"Range" "bytes=-4"}} 10)))) 

(describe "Partial content"

  (def partial-file
    {"/partial_file" (MemoryFile. "partial content" "text/plain")})

  (defn get-partial [headers]
    (do-get (merge {:path "/partial_file"} headers) partial-file))

  (it "returns 206 Partial Content if range header is present"
    (should= 206 (status-of (get-partial {:headers {"Range" "bytes=0-4"}})))) 

  (it "returns the correct content type"
    (should= "text/plain" (content-type (get-partial {:headers {"Range" "bytes=0-4"}}))))

  (it "serves n-n bytes of the file"
    (should= (to-byte-seq "rtial co") (body-seq (get-partial {:headers {"Range" "bytes=2-9"}}))))

  (it "returns the number of bytes read in n-n request"
     (should= 8 (content-length (get-partial {:headers {"Range" "bytes=2-9"}}))))  

  (it "serves all bytes from start to end when end is not specified"
     (should= (to-byte-seq "rtial content") (body-seq (get-partial {:headers {"Range" "bytes=2-"}}))))

  (it "returns the number of bytes read in n- request"
     (should= 13 (content-length (get-partial {:headers {"Range" "bytes=2-"}}))))  

  (it "serves last n bytes when no start is specified in range request"
     (should= (to-byte-seq "nt") (body-seq (get-partial {:headers {"Range" "bytes=-2"}}))))     

  (it "returns the number of bytes read in -n request"
     (should= 2 (content-length (get-partial {:headers {"Range" "bytes=-2"}})))) )
