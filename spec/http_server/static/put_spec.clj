(ns http_server.static.put_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.memory_file]
       [http_server.static.put])
  (:import http_server.static.memory_file.MemoryFile)) 

(def files {"/file1" (MemoryFile. "file1 content" "text/plain")})

(defn- put-path [path]
  (do-put {:path path} files))

(describe "put"
  (it "returns method not allowed"
    (should= 405 (status-of (put-path "/file1")))))
