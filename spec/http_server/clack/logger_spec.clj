(ns http_server.clack.logger_spec
  (use [http_server.clack.logger]
       [http_server.clack.methods]
       [http_server.spec_helper]
       [speclj.core]))

(defn next-app [env])

(defn get-logs []
  (logger "/logs" next-app {:method GET :path "/logs"}))

(defn call-logger [method path]
  (logger "/logs" next-app {:method method :path path}))

(describe "logger"
  (it "logs a request"
    (call-logger GET "/") 
    (should= "GET / HTTP/1.1" (body-of (get-logs))))

  (xit "logs many request"
    (call-logger GET "/") 
    (call-logger GET "/hello") 
    (call-logger POST "/hello") 
    (should-contain "GET / HTTP/1.1" (body-of (get-logs))))
    (should-contain "GET /hello HTTP/1.1" (body-of (get-logs))) 
    (should-contain "POST /hello HTTP/1.1" (body-of (get-logs))))
