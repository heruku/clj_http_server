(ns http-server.server_spec
  (:require [http-server.server :as server]
            [http_server.clack.core :as clack]
            [speclj.core :refer :all])
  (:import (java.io ByteArrayOutputStream StringReader BufferedReader Closeable)))

(def closeable (reify Closeable 
                 (close [this])))

(defn two-hundred-ok-app [_app _req]
  [200 {} ""])

(def out (atom (ByteArrayOutputStream. )))
(def request "GET / HTTP/1.1")

(defn fake-out [_socket]
  @out)

(defn fake-reader [_socket]
  (BufferedReader. (StringReader. request)))

(describe "server"
  (it "responds to a request"
    (with-redefs [server/get-output-stream fake-out 
                  server/get-reader        fake-reader]
  (clack/use-middleware two-hundred-ok-app)
  (server/process-request closeable clack/clack)
  (should= "HTTP/1.1 200 OK\r\n\r\n" (.toString @out)))))
