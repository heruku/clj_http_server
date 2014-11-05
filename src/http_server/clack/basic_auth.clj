(ns http_server.clack.basic_auth)

(defn extract-credentials [env]
  (-> (:headers env)
      (get "Authorization")
      (clojure.string/split #" ")
      (nth 1)))

(defn should-authorize? [protected-path env]
  (= protected-path (:path env)))

(defn credentials-present? [env]
  (contains? (:headers env) "Authorization"))

(defn credentials-match? [credentials env]
  (= credentials (extract-credentials env)))

(defn authorized? [credentials env]
  (and (credentials-present? env) (credentials-match? credentials env)))

(defn basic-auth [protected-path credentials next-app env]
  (if (not (should-authorize? protected-path env)) 
    (next-app env)
    (do (if (authorized? credentials env)
        (next-app env)
        [401 {} "Authentication required"]))))

(defn encoded [username pass]
  (let [encoder (java.util.Base64/getEncoder)]
    (.encodeToString encoder (.getBytes (str username ":" pass)))))

(defn init-basic-auth [protected-path username password]
  (partial basic-auth protected-path (encoded username password)))

