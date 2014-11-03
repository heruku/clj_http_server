(ns http_server.static.patch_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.memory_file]
       [http_server.static.file]
       [http_server.static.patch])
  (:import http_server.static.memory_file.MemoryFile)) 

(def files (atom {}))

(defn reset-files []
  (reset! files {"/patch-file" (MemoryFile. "text content\n" "text/plain")}))

(defn- good-patch [body]
  (do-patch {:path "/patch-file" :etag "79b7dfe6262691ad86e1e85add9791bc8a3af302" :body body} files))

(defn bad-patch []
  (do-patch {:path "/patch-file" :etag "bad"} files))

(defn patch-file-contents []
  (String. (contents (get @files "/patch-file"))))

(describe "patch"
  (before (reset-files))

  (context "successful patch"
    (it "returns no content header"
      (should= 204 (status-of (good-patch "body"))))

    (it "replaces body with patched content"
      (good-patch "patched")  
      (should= "patched" (patch-file-contents))))

  (context "conflict"
    (it "returns 409 Conflict"
      (should= 409 (status-of (bad-patch))))

    (it "does not change file contents"
      (bad-patch)
      (should= "text content\n" (patch-file-contents)))))
