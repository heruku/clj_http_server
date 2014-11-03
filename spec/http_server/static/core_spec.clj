(ns http_server.static.core_spec
  (use [speclj.core]
       [http_server.static.core]
       [http_server.static.get]
       [http_server.static.post]
       [http_server.static.put]
       [http_server.static.patch]
       [http_server.clack.methods]
       [http_server.static.memory_file])
  (:import http_server.static.memory_file.MemoryFile))

(defn get-path [path]
  {:method GET :path path})

(defn post-path [path]
  {:method POST :path path})

(defn put-path [path]
  {:method PUT :path path})

(defn patch-path [path]
  {:method PATCH :path path})

(def test-files (atom {"/file1" (MemoryFile. "file1contents" "text/plain")}))

(describe "static"
  (it "does get when request is get"
    (with-redefs [files test-files]
      (should-invoke do-get {} (app nil (get-path "/file1")))))

  (it "does post when request is post"
    (with-redefs [files test-files]
      (should-invoke do-post {} (app nil (post-path "/file1")))))

  (it "does put when request is put"
    (with-redefs [files test-files]
      (should-invoke do-put {} (app nil (put-path "/file1")))))

  (it "does patch when request is put"
    (with-redefs [files test-files]
      (should-invoke do-patch {} (app nil (patch-path "/file1")))))

  (it "does not do patch when request is get"
    (with-redefs [files test-files]
      (should-not-invoke do-patch {} (app nil (get-path "/file1"))))))
