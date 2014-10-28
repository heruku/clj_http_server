(ns http-server.core
  (use [http-server.server :as server])
  (:gen-class))

(defn -main [& args]
  (server/start 5000 "/Users/ukutaht/Desktop/code/http_server/cob_spec/public/"))
