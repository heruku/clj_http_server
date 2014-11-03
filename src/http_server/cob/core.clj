(ns http_server.cob.core)

(defn cob [next-app env]
  (if (= "/" (:path env))
    [200 {} ""]
    (next-app env)))
