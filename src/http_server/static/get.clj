(ns http_server.static.get
  (use [http_server.static.file]))

(defn do-get [request files]
  (let [file (get files (:path request))]
    [200 {"Content-Length" (length file)
          "Content-Type" (content-type file)} (contents file)]))
