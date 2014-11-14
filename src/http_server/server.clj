(ns http-server.server
  (:use [clojure.java.io :as io]
       [http_server.clack.core :as clack]
       [http_server.clack.logger :as logger]
       [http_server.static.core :as static]
       [http_server.clack.basic_auth :as basic]
       [http_server.cob.core :as cob]) 
  (:import [java.net ServerSocket]
          [java.util.concurrent Executors]))

(defn get-output-stream [socket]
  (io/output-stream socket))

(defn get-reader [sock]
  (io/reader sock))

(defn- send-sock [socket msg]
  (with-open [out (get-output-stream socket)]
    (.write out msg)
    (.flush out)))

(defn process-request [sock handler]
  (with-open [readr (get-reader sock)]
    (send-sock sock (handler readr))
    (.close sock)))

(defn- serve [port handler]
  (let [threads (Executors/newFixedThreadPool 30)]
    (with-open [server-sock (ServerSocket. port)]
      (loop []
        (let [sock (.accept server-sock)]
          (.execute threads #(process-request sock handler))
          (recur))))))

(defn start [port public-dir]
  (static/initialize-static public-dir)
  (clack/use-middleware (basic/init-basic-auth "/logs" "admin" "hunter2")
                        (logger/init-logger "/logs")
                         static/app 
                         cob/cob)
  (serve port clack))
