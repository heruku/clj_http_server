(ns http_server.static.get_spec
  (use [speclj.core]
       [http_server.static.get]))

(def files {"file1" "file1 content"
            "file2" "file2 content"
            "big-file" (apply str (take 10000 (repeat "a")))})

(defn- get-path [path]
  (do-get {:path path} files))

(describe "presence"
  (it "returns 404 OK for /foobar"
    (should= 404 (:status (get-path "/foobar"))))
  
  (it "returns 200 OK for file1"
    (should= 200 (:status (get-path "/file1"))))

  (it "returns 200 OK for file2"
    (should= 200 (:status (get-path "/file2")))))

(describe "content"
  (it "returns contents of file1"
    (should= "file1 content" (:content (get-path "/file1"))))

  (it "returns contents of file2"
    (should= "file2 content" (:content (get-path "/file2"))))
  )

(describe "content length header"
  (it "returns content length of small file"
      (should= 13 (get (:headers (get-path "/file1")) "Content-Length")))

  (it "returns content length of big file"
      (should= 10000 (get (:headers (get-path "/big-file")) "Content-Length"))))
