(ns http_server.static.get
  (use [http_server.static.file]))

(defn serve-partial [file [start end]]
  (let [length (inc (- end start))
        contents (content-range file start end)]

       [206 {"Content-Length" length
             "Content-Type" (content-type file)} contents]))

(defn serve-whole [file]
  [200 {"Content-Length" (length file)
        "Content-Type" (content-type file)} (contents file)]  )

(defn toInts [strings]
  (map #(Integer/parseInt %) strings))

; {:headers {"Range" "bytes=0-4"}} => [0 4]
(defn get-byte-range [request]
  (-> request
      (get :headers)
      (get "Range")
      (clojure.string/split #"=" 2)
      (nth 1)
      (clojure.string/split #"-" 2)
      toInts))

(defn do-get [request files]
  (let [file (get files (:path request))]
    (if (contains? (:headers request) "Range")
      (serve-partial file (get-byte-range request))
      (serve-whole file))))
