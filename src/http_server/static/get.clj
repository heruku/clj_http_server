(ns http_server.static.get
  (use [http_server.static.file]))

(defn- filename [request]
  (:path request))

(defn- success [file]
  [200 {"Content-Length" (length file)
        "Content-Type" (content-type file)} (contents file)])

(defn do-get [request files]
  (if (contains? files (filename request))
    (success (get files (filename request)))
    [404 {} ""]))
