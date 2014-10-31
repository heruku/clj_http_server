(ns http_server.clack.response_spec
  (use [speclj.core]
       [http_server.clack.response]))

(defn to-bytes [string]
  (bytes (byte-array (map byte string))))

(defn to-byte-seq [string]
  (seq (to-bytes string)))

(describe "raw response from env"
  (it "returns raw bytes"
    (should= (to-byte-seq "HTTP/1.1 200 OK\r\n\r\n") (seq (raw-response [200 {} ""]))))

  (it "contains the http version"
    (should-contain "HTTP/1.1" (String. (raw-response [200 {} ""]))))

  (context "status code"
    (it "200"
      (should-contain "200 OK" (String. (raw-response [200 {} ""]))))

    (it "404" 
      (should-contain "404 Not Found" (String. (raw-response [404 {} ""])))))

    (it "405" 
      (should-contain "405 Method Not Allowed" (String. (raw-response [405 {} ""])))) 
  
  (context "headers"
    (it "formats arbitrary headers in correct format"
      (let [res (String. (raw-response [200 {"Header" "value" "Another-Header" "AnotherValue"} ""]))]
      (should-contain "Header: value" res)
      (should-contain "Another-Header: AnotherValue" res))))
  
  (context "body"
     (it "contains the body"
       (should-contain "body" (String. (raw-response [200 {} (to-bytes "body")]))))) )
