(ns http_server.static.file)

(defprotocol File
  (contents [this])
  (content-type [this])
  (length [this])
  (replace-contents [this new-contents]))
