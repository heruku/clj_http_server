(ns http_server.clack.request
  (use http_server.clack.methods))

(defn parse-path [line-parts] (nth line-parts 1))

(defn parse-http-version [line-parts] (-> line-parts (nth 2) (subs 5)))

(def parse-method first)

(defn parse-request-line [reader]
  (let [line-parts (clojure.string/split (.readLine reader) #" ")]
    {:method       (parse-method line-parts)
     :path         (parse-path line-parts)
     :http_version (parse-http-version line-parts)}))

(defn raw-header-lines [reader]
  (doall (take-while (complement empty?)
                     (repeatedly #(.readLine reader)))))

(defn split-at-colon [string]
  (clojure.string/split string #":" 2))

(defn headers [reader]
  (->> (raw-header-lines reader)
       (map split-at-colon)
       flatten
       (map clojure.string/trim)
       (apply hash-map)))

(defn content-length [headers]
  (try
    (Integer/parseInt (get headers "Content-Length"))
    (catch NumberFormatException e
      0)))

(defn body [reader n-bytes]
  (let [char-ary (char-array n-bytes)]
    (.read reader char-ary 0 n-bytes)
    (String. char-ary)))

(defn extra-params [headers]
  {:etag (get headers "If-Match")})

(defn parse [reader]
  (let [request-line (parse-request-line reader)
        headers      (headers reader)
        body         (body reader (content-length headers))
        extra-params (extra-params headers)]
    (-> request-line 
        (assoc :headers headers)
        (assoc :body body)
        (merge extra-params))))
