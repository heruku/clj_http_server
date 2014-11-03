(ns http_server.clack.core_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.clack.core]))

(defn two-hundred-ok-app [app env]
  [200 {} ""])

(defn calls-through [next-app env]
  (next-app env))

(def simple-get "GET / HTTP/1.1")
(def ok-response "HTTP/1.1 200 OK\r\n\r\n")
(def not-found "HTTP/1.1 404 Not Found\r\n\r\n")

(describe "clack"
  (it "returns 200 ok for simple get"
    (use-middleware two-hundred-ok-app)
    (should= ok-response (String. (clack (request-reader simple-get)))))

  (it "threads the request through all of the apps"
    (use-middleware calls-through
                    calls-through
                    two-hundred-ok-app)
    (should= ok-response (String. (clack (request-reader simple-get)))))

  (it "threads the request through all of the apps"
    (def blow-up)
    (use-middleware calls-through
                    two-hundred-ok-app
                    blow-up)
    (should= ok-response (String. (clack (request-reader simple-get)))))

  (it "sends 404 if none of the apps return a response"
    (use-middleware calls-through
                    calls-through
                    calls-through)
    (should= not-found (String. (clack (request-reader simple-get))))))
