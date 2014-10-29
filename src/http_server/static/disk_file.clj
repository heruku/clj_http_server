(ns http_server.static.disk_file
  (use [http_server.static.file])) 

(defrecord DiskFile [path]
  File
  (contents [this] (slurp (:path this))))
