(ns http-server.server
  (use [clojure.java.io :as io]
       [http_server.clack.core :as clack]
       [http_server.static.core :as static])
  (import [java.net ServerSocket]))

(defn receive [socket]
  (.readLine (io/reader socket)))

(defn send-sock [socket msg]
  (println msg)
  (let [writer (io/writer socket)]
      (.write writer msg)
      (.flush writer)))

(defn serve [port handler]
  (loop []
    (with-open [server-sock (ServerSocket. port)
                sock (.accept server-sock)]
      (let [msg (receive sock)]
        (println msg)
        (send-sock sock (handler msg))))
    (recur)))

(defn start [port public-dir]
  (initialize-static public-dir)
  (clack/use-middleware static/app)
  (serve port clack))
