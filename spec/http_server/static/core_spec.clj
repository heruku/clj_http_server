(ns http_server.static.core_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.core]
       [http_server.static.get]
       [http_server.static.patch]
       [http_server.clack.methods]
       [http_server.static.memory_file])
  (:import http_server.static.memory_file.MemoryFile))

(def test-files (atom { "/file1" (MemoryFile. "file1contents" "text/plain")
                        "/file2" (MemoryFile. "file2contents" "text/plain")}))

(defn call-app [method path]
  (app nil {:method method :path path}))

(describe "static"
  (it "does get when request is get"
    (with-redefs [files test-files]
      (should-invoke do-get {} (call-app GET "/file1"))))

  (it "does patch when request is put"
    (with-redefs [files test-files]
      (should-invoke do-patch {} (call-app PATCH "/file1"))))

  (it "returns 405 method not allowed for all other methods"
    (with-redefs [files test-files]
      (should= 405 (status-of (call-app POST "/file1")))
      (should= 405 (status-of (call-app PUT "/file2")))
      (should= 405 (status-of (call-app DELETE "/file1")))))

  (context "index"
    (it "returns 200 ok"
      (should= 200 (status-of (call-app GET "/"))))

    (it "contains all files"
      (with-redefs [files test-files]
        (let [res-body (body-of (call-app GET "/"))]
          (should-contain "file1" res-body)
          (should-contain "file2" res-body))))

    (it "contains links to all files"
      (with-redefs [files test-files]
        (let [res-body (body-of (call-app GET "/"))]
          (should-contain "<a href=\"/file1\">file1</a>" res-body)
          (should-contain "<a href=\"/file2\">file2</a>" res-body))))))
