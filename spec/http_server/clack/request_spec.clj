(ns http_server.clack.request_spec
  (use [speclj.core]
       [http_server.clack.request]
       [http_server.clack.methods]))

(describe "parsing"
  (context "method"
    (it "parses get"
      (should= GET (:method (parse "GET / HTTP/1.1")))))

    (it "parses post"
      (should= POST (:method (parse "POST / HTTP/1.1")))) 

  (it "parses root"
    (should= "/" (:path (parse "GET / HTTP/1.1"))))

  (it "parses path"
    (should= "/hello/world" (:path (parse "GET /hello/world HTTP/1.1"))))

  (it "parses http version"
    (should= "1.1" (:http_version (parse "GET /hello/world HTTP/1.1")))))
