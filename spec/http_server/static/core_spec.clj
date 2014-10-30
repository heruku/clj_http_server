(ns http_server.static.core_spec
  (use [speclj.core]
       [http_server.static.core]
       [http_server.static.get]
       [http_server.static.post]
       [http_server.static.put]
       [http_server.clack.methods]
       [http_server.static.memory_file])
  (:import http_server.static.memory_file.MemoryFile))

(defn get-path [path]
  {:method GET :path path})

(defn post-path [path]
  {:method POST :path path})

(defn put-path [path]
  {:method PUT :path path})

(def test-files (atom {"/file1" (MemoryFile. "file1contents" "text/plain")}))

(describe "static"
  (with-redefs [files test-files] 
    (it "does get when request is get"
      (should-invoke do-get {} (app nil (get-path "/file1"))))

    (it "does post when request is post"
      (should-invoke do-post {} (app nil (post-path "/file1"))))))

    (it "does put when request is put"
      (should-invoke do-put {} (app nil (put-path "/file1"))))  
