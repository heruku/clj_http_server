(ns http_server.request_spec
  (use [speclj.core]
       [http_server.request]))

(describe "parsing"
  (context "method"
    (it "parses get"
      (should= GET (:method (parse "GET / HTTP/1.1")))))

  (it "parses root"
    (should= "/" (:path (parse "GET / HTTP/1.1"))))

  (it "parses path"
    (should= "/hello/world" (:path (parse "GET /hello/world HTTP/1.1"))))

  (it "parses http version"
    (should= "1.1" (:http_version (parse "GET /hello/world HTTP/1.1"))))
  )
