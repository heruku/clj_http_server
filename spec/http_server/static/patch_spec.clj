(ns http_server.static.patch_spec
  (use [speclj.core]
       [http_server.static.memory_file]
       [http_server.static.patch])
  (:import http_server.static.memory_file.MemoryFile)) 

(def files {"/patch-file" (MemoryFile. "patch file content" "text/plain")})

(defn status-of [response]
  (nth response 0))

(defn- patch-path [path]
  (do-patch {:path path} files))

(describe "post"
  (it "returns method not allowed"
    (should= 204 (status-of (patch-path "/patch-file")))))
