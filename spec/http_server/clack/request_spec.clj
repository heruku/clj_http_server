(ns http_server.clack.request_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.clack.request]
       [http_server.clack.methods]))

(defn call-parse [raw-request-str]
  (parse (request-reader raw-request-str)))

(describe "parsing"
  (context "method"
    (it "parses get"
      (should= GET (:method (call-parse "GET / HTTP/1.1")))))

    (it "parses post"
      (should= POST (:method (call-parse "POST / HTTP/1.1")))) 

  (it "parses root"
    (should= "/" (:path (call-parse "GET / HTTP/1.1"))))

  (it "parses path"
    (should= "/hello/world" (:path (call-parse "GET /hello/world HTTP/1.1"))))

  (it "path does not include query parameters"
    (should= "/hello/world" (:path (call-parse "GET /hello/world?q=1 HTTP/1.1"))))

  (it "parses the query string"
    (should= "q=hello&hello=world" (:query-string (call-parse "GET /hello/world?q=hello&hello=world HTTP/1.1"))))

  (it "parses http version"
    (should= "HTTP/1.1" (:http_version (call-parse "GET /hello/world HTTP/1.1"))))

  (it "parses a header"
    (should= {"From" "uku.taht@gmail.com"} (:headers (call-parse "GET /hello/world HTTP/1.1\r\nFrom:uku.taht@gmail.com\r\n"))))

  (it "parses multiple headers"
    (should= {"From" "uku.taht@gmail.com"
              "Content-Length" "5"} (:headers (call-parse "GET /hello/world HTTP/1.1\r\nFrom:uku.taht@gmail.com\r\nContent-Length:5\r\n"))))

  (it "trims whitespace from headers"
    (should= {"From" "uku.taht@gmail.com" } (:headers (call-parse "GET /hello/world HTTP/1.1\r\nFrom  :   uku.taht@gmail.com\r\n"))))

  (it "can handle header values with colons in them"
    (should= {"Host" "localhost:5000" } (:headers (call-parse "GET /hello/world HTTP/1.1\r\nHost: localhost:5000\r\n"))))

  (it "reads the body"
    (should= "thebody" (:body (call-parse "GET /hello/world HTTP/1.1\r\nContent-Length: 7\r\n\r\nthebody\r\n"))))

  (context "convenience"
    (it "parses the etag header"
      (should= "etag" (:etag (call-parse "GET / HTTP/1.1\r\nIf-Match: etag\r\n\r\n"))))

    (it "parses the params from query params"
      (should= {"q" "hello" "hello" "world"} (:params (call-parse "GET /page?q=hello&hello=world HTTP/1.1\r\nIf-Match: etag\r\n\r\n"))))

    (it "handles empty parameters"
      (should= {"q" "" "hello" ""} (:params (call-parse "GET /page?q=&hello= HTTP/1.1\r\nIf-Match: etag\r\n\r\n"))))

    (it "decodes parameters"
      (should= {"q" "<>?"} (:params (call-parse "GET /page?q=%3C%3E%3F HTTP/1.1\r\nIf-Match: etag\r\n\r\n"))))

    (it "parses the host header"
      (should= "host" (:host (call-parse "GET / HTTP/1.1\r\nHost: host\r\n\r\n"))))))  
