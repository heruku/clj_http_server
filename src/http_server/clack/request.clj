(ns http_server.clack.request
  (use http_server.clack.methods))

(defn split-request-uri [line-parts]
  (clojure.string/split (second line-parts) #"\?"))

(defn parse-request-line [reader]
  (let [line-parts (clojure.string/split (.readLine reader) #" ")
        [path q-string] (split-request-uri line-parts)]
    {:method       (first line-parts)
     :path         path
     :query-string q-string
     :http_version (nth line-parts 2)}))

(defn raw-header-lines [reader]
  (doall (take-while (complement empty?)
                     (repeatedly #(.readLine reader)))))

(defn split-at-colon [string]
  (clojure.string/split string #":" 2))

(defn headers [reader]
  (->> (raw-header-lines reader)
       (mapcat split-at-colon)
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

(defn extract-param-pairs [string]
  (clojure.string/split string #"&"))

(defn split-param-values [pair]
  (clojure.string/split pair #"="))

(defn pad-if-no-value [pair]
  [(first pair) (or (second pair) "")])

(defn extract-params [request-line]
  (if-let [q-string (:query-string request-line)]
    (->> q-string
         extract-param-pairs
         (map split-param-values)
         (mapcat pad-if-no-value)
         (map #(java.net.URLDecoder/decode %))
         (apply hash-map))))

(defn extra-params [request-line headers]
  {:etag (get headers "If-Match")
   :host (get headers "Host")
   :params (extract-params request-line)})

(defn parse [reader]
  (let [request-line (parse-request-line reader)
        headers      (headers reader)
        body         (body reader (content-length headers))
        extra-params (extra-params request-line headers)]
    (-> request-line 
        (assoc :headers headers)
        (assoc :body body)
        (merge extra-params))))
