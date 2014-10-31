(ns http_server.clack.response)

(def http-version
  "HTTP/1.1")

(def line-separator 
  "\r\n")

(defn response-code [status]
  (case status
    200 "200 OK"
    405 "405 Method Not Allowed"
    404 "404 Not Found"))

(defn status-line [status]
  (str http-version " " (response-code status) line-separator))

(defn header-from [[k v]]
  (str k ": " v line-separator))

(defn header-lines [headers]
  (apply str (map header-from (vec headers))))

(defn raw-response [[status headers body]]
  (.getBytes (str (status-line  status)
       (header-lines headers)
       line-separator
       body)))
