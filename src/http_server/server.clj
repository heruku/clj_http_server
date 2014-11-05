(ns http-server.server
  (use [clojure.java.io :as io]
       [http_server.clack.core :as clack]
       [http_server.clack.logger :as logger]
       [http_server.static.core :as static]
       [http_server.clack.basic_auth :as basic]
       [http_server.cob.core :as cob]) 
  (import [java.net ServerSocket]
          [java.util.concurrent Executors]))

(defn receive [socket]
  (io/reader socket))

(defn send-sock [socket msg]
  (with-open [writer (io/output-stream socket)]
    (.write writer msg)
    (.flush writer)))

(defn process-request [sock handler]
  (with-open [request (receive sock)]
    (send-sock sock (handler request))
    (.close sock)))

(defn serve [port handler]
  (let [threads (Executors/newFixedThreadPool 30)]
    (with-open [server-sock (ServerSocket. port)]
      (loop []
        (let [sock (.accept server-sock)]
          (.execute threads (partial process-request sock handler))
          (recur))))))

(defn start [port public-dir]
  (static/initialize-static public-dir)
  (clack/use-middleware (basic/init-basic-auth "/logs" "admin" "hunter2")
                        (logger/init-logger "/logs")
                         static/app 
                         cob/cob)
  (serve port clack))
