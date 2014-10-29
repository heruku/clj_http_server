(ns http_server.clack.core_spec
  (use [speclj.core]
       [http_server.clack.core]))

(defn two-hundred-ok-app [app env]
  [200 {} ""])

(describe "clack"
  (it "returns 200 ok for simple get"
    (use-middleware two-hundred-ok-app)
    (should= "HTTP/1.1 200 OK" (clack "GET / HTTP/1.1"))))
