(ns http-server.core
  (use [http-server.server :as server]
       [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))


(def cli-options
  [["-p" "--port PORT" "Port number" :default 5000 :parse-fn #(Integer/parseInt %)]
   ["-d" "--public-dir PUBLIC_DIR" "" :default "cob_spec/public"] ])

(defn -main [& args]
  (let [{:keys [options summary]} (parse-opts args cli-options)]
    (println options)
    (server/start (:port options) (:public-dir options))))
