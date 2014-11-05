(ns http_server.clack.response_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.clack.response]))

(describe "raw response from env"
  (it "returns raw bytes"
    (should= (to-byte-seq "HTTP/1.1 200 OK\r\n\r\n") (seq (raw-response [200 {} ""]))))

  (it "contains the http version"
    (should-contain "HTTP/1.1" (String. (raw-response [200 {} ""]))))

  (context "status code"
    (it "200"
      (should-contain "200 OK" (String. (raw-response [200 {} ""]))))

    (it "204"
      (should-contain "204 No Content" (String. (raw-response [204 {} ""]))))

    (it "206"
      (should-contain "206 Partial Content" (String. (raw-response [206 {} ""]))))

    (it "301"
      (should-contain "301 Moved Permanently" (String. (raw-response [301 {} ""]))))

    (it "302"
      (should-contain "302 Found" (String. (raw-response [302 {} ""]))))

    (it "401"
      (should-contain "401 Unauthorized" (String. (raw-response [401 {} ""]))))

    (it "404" 
      (should-contain "404 Not Found" (String. (raw-response [404 {} ""]))))

    (it "405" 
      (should-contain "405 Method Not Allowed" (String. (raw-response [405 {} ""])))) 

    (it "409" 
      (should-contain "409 Conflict" (String. (raw-response [409 {} ""]))))) 
  
  (context "headers"
    (it "formats arbitrary headers in correct format"
      (let [res (String. (raw-response [200 {"Header" "value" "Another-Header" "AnotherValue"} ""]))]
      (should-contain "Header: value" res)
      (should-contain "Another-Header: AnotherValue" res))))
  
  (context "body"
     (it "contains the body if it comes in as string"
       (should-contain "body" (String. (raw-response [200 {} "body"]))))   
     (it "contains the body if it comes in as bytes"
       (should-contain "body" (String. (raw-response [200 {} (to-bytes "body")]))))) )
