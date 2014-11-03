(ns http_server.static.patch
  (use [http_server.static.util]
       [http_server.static.file]))

(defn sha-sum-matches? [file etag]
  (= (sha-sum (contents file)) etag))

(defn success [env files]
  (swap! files assoc (:path env) (replace-contents (get @files (:path env)) (:body env)))
  [204 {} ""])

(defn do-patch [env files]
  (let [file (get @files (:path env))
        etag (:etag env)]
    (if (sha-sum-matches? file etag)
      (success env files)
      [409 {} ""])))
