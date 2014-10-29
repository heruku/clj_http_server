(ns http_server.clack.response)

(def http-version
  "HTTP/1.1")

(defn response-code [status]
  (case status
    200 "200 OK"
    404 "404 Not Found"))

(defn status-line [status]
  (str http-version " " (response-code status)))

(defn header-from [[k v]]
  (str k ": " v "\n"))

(defn header-lines [headers]
  (apply str (map header-from (vec headers))))

(defn raw-response [[status headers body]]
  (str (status-line  status)
       (header-lines headers)
       body))
