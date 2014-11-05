(ns http_server.static.core
  (use [http_server.static.get]
       [http_server.static.post]
       [http_server.static.put]
       [http_server.static.patch]
       [http_server.clack.methods]
       [http_server.static.disk_file])
  (import http_server.static.disk_file.DiskFile))

(def files (atom {}))

(defn add-file [java-file dirname]
  (let [abs-path (.getAbsolutePath java-file)
        rel-path (subs abs-path (count dirname))
        file     (DiskFile. abs-path)]
    (swap! files assoc rel-path file)))

(defn initialize-static [dirname]
  (doseq [file  (file-seq (clojure.java.io/file dirname))]
    (if (.isFile file)
      (add-file file dirname))))

(defn call-method [env]
 (cond 
  (= (:method env) GET) (do-get env @files)
  (= (:method env) POST) (do-post env @files)
  (= (:method env) PUT) (do-put env @files)
  (= (:method env) PATCH) (do-patch env files)))

(defn link [filename]
  (str "<a href=\"" filename "\">" (subs filename 1) "</a>"))

(defn make-links [files]
  (->> (keys files)
       (map link)
       (clojure.string/join "\n")))

(defn index []
  [200 {} (make-links @files)])

(defn index-request? [{:keys [path method]}]
  (and (= method GET) (= path "/")))

(defn app [next-app env]
  (cond 
    (index-request? env)           (index)
    (contains? @files (:path env)) (call-method env)
    :else                          (next-app env)))
