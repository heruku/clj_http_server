(ns http-server.server
  (use [clojure.java.io :as io])
  (import [java.net ServerSocket]))

(def ok 
  (str "HTTP/1.1 200 OK" "\r\n"))

(defn receive [socket]
  (.readLine (io/reader socket)))

(defn send-sock [socket msg]
  (let [writer (io/writer socket)]
      (.write writer msg)
      (.flush writer)))

(defn start [port public-dir]
  (loop []
    (with-open [server-sock (ServerSocket. port)
                sock (.accept server-sock)]
      (let [msg (receive sock)]
        (send-sock sock ok)))
    (recur)))
