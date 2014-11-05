(ns http_server.static.file)

(defprotocol File
  (contents [this])
  (content-range [this start end])
  (content-type [this])
  (length [this])
  (replace-contents [this new-contents]))
