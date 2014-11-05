(ns http_server.cob.core
  (use [http_server.clack.methods]))

(def form-data (atom ""))

(defn show-options [env]
  [200 {"Allow" "GET,HEAD,POST,OPTIONS,PUT"} ""])

(defn get-form [env]
  [200 {} @form-data])

(defn post-form [env]
  (reset! form-data (:body env))
  [200 {} ""])

(defn put-form [env]
  (reset! form-data (:body env))
  [200 {} ""])

(defn delete-form [env]
  (reset! form-data "")
  [200 {} ""])

(defn join-params [params]
  (->> (vec params)
       (map (partial clojure.string/join " = "))
       (clojure.string/join "\n")))

(defn get-parameters [env]
  [200 {} (join-params (:params env))])

(defn get-redirect [env]
  [301 {"Location" (str "http://" (:host env) "/")} ""])

(def routes
  {[OPTIONS "/method_options"] show-options
   [GET     "/form"          ] get-form
   [POST    "/form"          ] post-form
   [PUT     "/form"          ] put-form
   [DELETE  "/form"          ] delete-form
   [GET     "/parameters"    ] get-parameters
   [GET     "/redirect"      ] get-redirect})  

(defn cob [next-app env]
  (let [route [(:method env) (:path env)]]
    (if (contains? routes route)
      ((get routes route) env)
      (next-app env))))
