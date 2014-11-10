(ns http_server.clack.response)

(def http-version
  "HTTP/1.1")

(def line-separator 
  "\r\n")

(def status-codes 
  {200 "200 OK"
   204 "204 No Content"
   206 "206 Partial Content"
   301 "301 Moved Permanently"
   302 "302 Found"
   401 "401 Unauthorized"
   404 "404 Not Found"
   405 "405 Method Not Allowed"
   409 "409 Conflict"})

(defn response-code [status]
  (get status-codes status))

(defn status-line [status]
  (str http-version " " (response-code status) line-separator))

(defn header-from [[k v]]
  (str k ": " v line-separator))

(defn header-lines [headers]
  (apply str (map header-from (vec headers))))

(defn merge-bytes [b1 b2]
  (with-open [out (new java.io.ByteArrayOutputStream)]
    (.write out b1 0 (count b1))
    (.write out b2 0 (count b2))
    (.toByteArray out)))

(defn leading-bytes [status headers]
  (.getBytes (str (status-line status)
                  (header-lines headers)
                  line-separator)))

(defn bytes-if-string [body]
  (if (instance? String body)
    (byte-array (map byte body))
    body))

(defn raw-response [[status headers body]]
  (merge-bytes (leading-bytes status headers) (bytes-if-string body)))
