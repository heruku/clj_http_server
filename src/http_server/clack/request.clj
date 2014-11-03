(ns http_server.clack.request
  (use http_server.clack.methods))

(defn parse-path [request-line]
  (-> request-line
      (clojure.string/split #" ")
      (nth 1)))

(defn parse-http-version [request-line]
  (-> request-line
      (clojure.string/split #" ")
      (nth 2)
      (subs 5)) )

(defn parse-method [request-line]
  (-> request-line
      (clojure.string/split #" ")
      first))

(defn request-line [line]
  {:method       (parse-method line)
   :path         (parse-path line)
   :http_version (parse-http-version line)})

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defn raw-header-lines [reader]
  (doall (take-while (complement empty?)
                     (repeatedly #(.readLine reader)))))

(defn split-at-colon [string]
  (clojure.string/split string #":"))

(defn headers [reader]
  (->> (raw-header-lines reader)
       (map split-at-colon)
       flatten
       (map clojure.string/trim)
       (apply hash-map)))

(defn parse [reader]
  (assoc (request-line (.readLine reader))
         :headers (headers reader)))
