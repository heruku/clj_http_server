(ns http_server.clack.logger
  (use [http_server.clack.methods]))

(def logs (atom ()))

(defn format-request [env]
  (str (:method env) " " (:path env) " " (:http_version env)))

(defn log-request [env]
  (swap! logs conj (format-request env)))

(defn show-logs? [path env]
  (and (= path (:path env))
       (= GET  (:method env))))

(defn logger [path next-app env]
  (if (show-logs? path env) 
    [200 {} (clojure.string/join "\n" @logs)]
    (do (log-request env)
         (next-app env))))

(defn init-logger [path]
  (partial logger path))
