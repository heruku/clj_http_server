(ns http_server.clack.response_spec
  (use [speclj.core]
       [http_server.clack.response]))

(describe "raw response from env"
  (it "contains the http version"
    (should-contain "HTTP/1.1" (raw-response [200 {} ""])))

  (context "status code"
    (it "200"
      (should-contain "200 OK" (raw-response [200 {} ""])))

    (it "404" 
      (should-contain "404 Not Found" (raw-response [404 {} ""]))))
  
  (context "headers"
    (it "formats arbitrary headers in correct format"
      (let [res (raw-response [200 {"Header" "value" "Another-Header" "AnotherValue"} ""])]
      (should-contain "Header: value" res)
      (should-contain "Another-Header: AnotherValue" res))))
  
  (context "body"
     (it "contains the body"
       (should-contain "body" (raw-response [200 {} "body"])))) 
  )
