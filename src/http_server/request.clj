(ns http_server.request)

(def GET :get)

(defn parse-path [raw-request]
  (-> raw-request
      (clojure.string/split #" ")
      (nth 1)))

(defn parse-http-version [raw-request]
  (-> raw-request
      (clojure.string/split #" ")
      (nth 2)
      (subs 5)) )

(defn parse [raw-request]
  {:method       GET
   :path         (parse-path raw-request)
   :http_version (parse-http-version raw-request)})
