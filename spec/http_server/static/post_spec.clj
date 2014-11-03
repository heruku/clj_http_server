(ns http_server.static.post_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.memory_file]
       [http_server.static.post])
  (:import http_server.static.memory_file.MemoryFile)) 

(def files {"/file1" (MemoryFile. "file1 content" "text/plain")})

(defn- post-path [path]
  (do-post {:path path} files))

(describe "post"
  (it "returns method not allowed"
    (should= 405 (status-of (post-path "/file1")))))
