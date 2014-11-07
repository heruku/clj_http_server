(ns http_server.static.get
  (use [http_server.static.file]))

(def file-end (.toString Integer/MAX_VALUE))

(defn serve-partial [file [start end]]
  (let [content-length (inc (- end start))
        contents       (content-range file start end)]

       [206 
        {"Content-Length" content-length
         "Content-Range" (str start "-" end "/" (length file))
         "Content-Type"   "video/mp4"} 
        contents]))

(defn serve-whole [file]
  [200 
   {"Content-Length" (length file)
    "Content-Type"   "video/mp4"}
   (contents file)])

(defn toInts [strings]
  (map #(Integer/parseInt %) strings))

(defn replace-missing [[start end] file-length]
  (cond
    (empty? end)    [start (.toString file-length)]
    (empty? start)  [(.toString (- file-length (dec (Integer/parseInt end)))) (.toString file-length)]
    :else [start end])
  )

; {:headers {"Range" "bytes=0-4"}} => [0 4]
(defn get-byte-range [request file]
  (-> (get-in request [:headers "Range"])
      (clojure.string/split #"=" 2)
      (nth 1)
      (clojure.string/split #"-" 2)
      (replace-missing (dec (length file)))
      toInts))

(defn do-get [request files]
  (println request)
  (let [file (get files (:path request))]
    (if (contains? (:headers request) "Range")
      (serve-partial file (get-byte-range request file))
      (serve-whole file))))
