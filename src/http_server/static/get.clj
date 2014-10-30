(ns http_server.static.get
  (use [http_server.static.file]))

(defn- filename [request]
  (:path request))

(defn- success [content]
  [200 {"Content-Length" (count content)} content])

(defn do-get [request files]
  (map println files)
  (if (contains? files (filename request))
    (success (contents (get files (filename request))))
    [404 {} ""]))
