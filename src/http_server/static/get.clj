(ns http_server.static.get
  (use [http_server.static.file]))

(defn serve-partial [file [start end]]
  (let [content-length (inc (- end start))
        contents       (content-range file start end)]

       [206 
        {"Content-Length" content-length
         "Content-Range" (str start "-" end "/" (length file))
         "Content-Type"   (content-type file)}
        contents]))

(defn serve-whole [file]
  [200 
   {"Content-Length" (length file)
    "Content-Type"   (content-type file)}
   (contents file)])

(defn to-int [string]
  (if (empty? string) 
     nil
    (Integer/parseInt string)))

(defn to-ints [strings]
  (map to-int strings))

(defn replace-missing [[start end] file-length]
  (let [end (or end file-length)]
    (if start
      [start end]
      [(- file-length (dec end)) file-length])))

(defn get-byte-range [request file-length]
  (-> (get-in request [:headers "Range"])
      (clojure.string/split #"=" 2)
       second
      (clojure.string/split #"-" 2)
      to-ints
      (replace-missing (dec file-length))))

(defn do-get [request files]
  (let [file (get files (:path request))]
    (if (contains? (:headers request) "Range")
      (serve-partial file (get-byte-range request (length file)))
      (serve-whole file))))
